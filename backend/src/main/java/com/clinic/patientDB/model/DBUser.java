package com.clinic.patientDB.model;

import com.clinic.patientDB.auth.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.*;

@Entity
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
public class DBUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    @Column(name = "first_name")
//    private String firstName;
//    @Column(name = "last_name")
//    private String lastName;
    @Column(name = "username", columnDefinition = "VARCHAR(30)", nullable = false)
    private String username;
    @Column(name = "password", columnDefinition = "VARCHAR(20)", nullable = false)
    private String password;
    @Column(name = "role", columnDefinition = "VARCHAR(30)", nullable = false)
    private String role;


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DBUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public DBUser() {
    }

    public static final class UserBuilder {
        private Long id;
        private String username;
        private String password;
        private String role;

        private UserBuilder() {
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(String role) {
            this.role = role;
            return this;
        }

        public DBUser build() {
            DBUser DBUser = new DBUser();
            DBUser.setUsername(username);
            DBUser.setPassword(password);
            DBUser.setRole(role);
            DBUser.id = this.id;
            return DBUser;
        }
    }
}
