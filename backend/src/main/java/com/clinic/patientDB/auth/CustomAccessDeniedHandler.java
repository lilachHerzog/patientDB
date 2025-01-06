package com.clinic.patientDB.auth;

import com.clinic.patientDB.model.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
//        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Role currentRole = Role.fromString(authentication.getAuthorities().iterator().next().getAuthority());
            String methodFromRequenst = request.getRequestURI().replace("/auth/", "");

            for ( Method method :AuthController.class.getDeclaredMethods()) {
                if (method.getName().equals(methodFromRequenst)) {

                    Role requiredRole = Role.valueOf(extractRequiredRoleFromAnnotation(method));

                    String errorMessage = String.format("""
                                {
                                    "error": "Access Denied",
                                    "message": "The required authorization level is %d (%s). Your authorization level is %d (%s)."
                                }
                            """, requiredRole.getLevel(), requiredRole, currentRole.getLevel(), currentRole);

                    response.getWriter().write(errorMessage);
                    return;
                }
            }
            throw new IOException("no such method \'" + methodFromRequenst + "\'");
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }
    }


    public String extractRequiredRoleFromAnnotation(Method method) {
        PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
        if (preAuthorize != null) {
            String expression = preAuthorize.value(); //
            Pattern pattern = Pattern.compile("hasRole\\(\\'ROLE_([a-zA-Z]*)\\'\\)");
            Matcher matcher = pattern.matcher(expression);
            boolean roleFound = matcher.find();
            if(roleFound) {
                return matcher.group(1);
            }

        }
        return "Unknown Role";
    }

}

