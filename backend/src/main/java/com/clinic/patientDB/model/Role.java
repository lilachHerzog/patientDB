package com.clinic.patientDB.model;

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

    @Component
    public class RoleChecker {
        public boolean hasPermission(Role userRole, Role requiredRole) {
            return userRole.getLevel() >= requiredRole.getLevel();
        }
    }


}
