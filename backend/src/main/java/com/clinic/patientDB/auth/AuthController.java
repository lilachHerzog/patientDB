package com.clinic.patientDB.auth;

import com.clinic.patientDB.model.DBUser;
import com.clinic.patientDB.model.Role;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;


    private JwtUtil jwtUtil;
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }



    @ResponseBody
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam String username) {
        DBUser user = userDetailsService.getDBUserByUsername(username).orElseThrow();
        Role role = Role.valueOf(user.getRole());
        System.out.println(role.name() + role.getLevel());
        return ResponseEntity.ok(user);
    }



    @GetMapping("/my-role")
    public ResponseEntity<String> getMyRole(HttpServletRequest request, Authentication auth) {
        Claims claims = jwtUtil.resolveClaims(request);
        System.out.println("User Authorities: " + auth.getAuthorities());

        String role = claims.get("role").toString();
        return ResponseEntity.ok( "role: " + role + ", reachables: " + jwtUtil.getReachacles(role));
    }

    @ResponseBody
    @GetMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        try {
            return ResponseEntity.ok(userDetailsService.deleteUser(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @ResponseBody
    @GetMapping("/deleteUserById")
    public ResponseEntity<?> deleteUserById(@RequestParam Long userId) {
        try {
            userDetailsService.deleteUser(userId);
            return ResponseEntity.ok("deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/addUser")
    @PreAuthorize("@roleChecker.hasPermission(authentication.principal.role, T(com.example.Role).DOCTOR)")
    public ResponseEntity<?> addUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
                UserDetails user = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                .username(username)
                .password(password)
                .roles(role)
                .build();
        return ResponseEntity.ok(new InMemoryUserDetailsManager(user));
    }
    @ResponseBody
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userDetailsService.findAll());

    }



    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginReq loginReq)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
            String username = authentication.getName();
            DBUser DBUser = userDetailsService.getDBUserByUsername(username).orElseThrow();
            String token = jwtUtil.createToken(DBUser);
            LoginRes loginRes = new LoginRes(username,token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}