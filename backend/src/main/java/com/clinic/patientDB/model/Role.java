package com.clinic.patientDB.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

public enum Role {
    ADMIN(3),
    DOCTOR(2),
    NURSE(1),
    SECRETARY(0);

    private final int level;

    Role(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static Role fromString(String roleName) {
        return Role.valueOf(roleName.replace("ROLE_", "").toUpperCase());
    }
    @Component
    public class RoleChecker {
        public boolean hasPermission(String userRole, String requiredRole) {
            return Role.valueOf(userRole).getLevel() >= Role.valueOf(requiredRole).getLevel();
        }
    }
}


