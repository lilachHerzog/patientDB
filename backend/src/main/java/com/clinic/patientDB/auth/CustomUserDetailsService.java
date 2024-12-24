package com.clinic.patientDB.auth;

import com.clinic.patientDB.model.DBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findUserByUsername(username);
        List<DBUser> DBUsers = userRepository.getDBUsersByUsername(username);
        if (DBUsers.size() == 0) {
            throw new UsernameNotFoundException("no user with username \'" + username + "\' found");
        }
        if (DBUsers.size() > 1) {
            throw new UsernameNotFoundException("there are " + DBUsers.size() + " users with username \'" + username + "\'");
        }
        DBUser user = DBUsers.get(0);
        List<String> roles = new ArrayList<>();
//        roles.add("ADMIN");
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