package com.clinic.patientDB.auth;

import com.clinic.patientDB.model.DBUser;
import com.clinic.patientDB.model.Role;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.AuthenticationException;

@Component
public class JwtUtil {


    private final String secret_key = "mysecretkey";
    private long accessTokenValidity = 60*60*1000;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String prefix = "ROLE_";

    public static RoleHierarchyImpl roleHierarchy;
    public JwtUtil(){
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        // יצירת ההיררכיה מתוך Enum
        String hierarchy =
                Arrays.stream(Role.values())
                        .sorted(Comparator.comparingInt(Role::getLevel).reversed())
                        .map(role -> prefix + role.name())
                        .collect(Collectors.joining(" > "));
        roleHierarchy.setHierarchy(hierarchy);
        System.out.println("RoleHierarchy: " + hierarchy);
        this.roleHierarchy = roleHierarchy;
    }

    public String createToken(DBUser user) {
        Role role = Role.valueOf(user.getRole());
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());
        claims.put("level", role.getLevel());

//        Claims claims = Jwts.claims().setSubject(DBUser.getUsername());
//        claims.put("firstName", DBUser.getFirstName());
//        claims.put("lastName", DBUser.getLastName());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }
    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

//    @Bean
//    public static RoleHierarchyImpl roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//
//        // יצירת ההיררכיה מתוך Enum
//        String hierarchy =
//                Arrays.stream(Role.values())
//                        .sorted(Comparator.comparingInt(Role::getLevel).reversed())
//                        .map(role -> role.name())
//                        .collect(Collectors.joining(" > "));
//        roleHierarchy.setHierarchy(hierarchy);
//        System.out.println("RoleHierarchy: " + hierarchy);
//
//        return roleHierarchy;
//    }



    public Collection<GrantedAuthority> getReachacles(String role){
        return roleHierarchy.getReachableGrantedAuthorities(Collections.singletonList(new SimpleGrantedAuthority(prefix + role)));
    }
}
