package com.clinic.patientDB.auth;

import com.clinic.patientDB.model.DBUser;
import com.clinic.patientDB.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @SuppressWarnings("deprecation")
    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        List<DBUser> DBUsers = userRepository.getDBUsersByUsername(username);
        if (DBUsers.size() == 0) {
            throw new UsernameNotFoundException("no user with username \'" + username + "\' found");
        }
        if (DBUsers.size() > 1) {
            throw new BadCredentialsException("there are " + DBUsers.size() + " users with username \'" + username + "\'");
        }
        DBUser user = DBUsers.get(0);
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build();
        return userDetails;
    }

    public DBUser deleteUser(String username) {
        DBUser user = getDBUserByUsername(username).orElseThrow();
        userRepository.delete(user);
        return user;
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);

    }

        public Optional<DBUser> getDBUserByID(Long id) {
        return userRepository.findById(id);
    }

    public Optional<DBUser> getDBUserByUsername(String username) {
        return userRepository.getDBUserByUsername(username);
    }

    public List<DBUser> findAll() {
        return userRepository.findAll();
    }

    public String getRoleFromClaims(Collection<? extends GrantedAuthority> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new BadCredentialsException("no roles found");
        }
        if (roles.size() > 1) {
            throw new BadCredentialsException("there are " + roles.size() + " roles found");
        }
        return roles.iterator().next().getAuthority().toUpperCase();
    }

    public DBUser save(String username, String password, String role) {
        Optional<DBUser> optionalDBUser = getDBUserByUsername(username);
        if (optionalDBUser.isPresent()) {
            throw new BadCredentialsException("ERROR! username \'" + username + "\' already exists");
        }
        DBUser user = new DBUser( username, passwordEncoder.encode(password), role.toUpperCase());
        userRepository.save(user);
        return user;
    }

//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin123")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
////    @Bean
////    public CommandLineRunner createAdminUser() {
////        return args -> {
////            if (userRepository.getUserByUsername("admin").size() == 0) {
////
////                User adminUser = new User();
////                adminUser.setUsername("admin");
////                adminUser.setPassword("admin123"); // סיסמת ברירת מחדל
////                adminUser.setRole("ADMIN");
////                userRepository.save(adminUser);
////            }
////        };
////    }
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(org.springframework.security.core.userdetails.User.withUsername("admin")
//                .password("admin123")
//                .roles(Role.ADMIN.name()) // תפקיד מנהל
//                .build());
//        return manager;
//    }

}