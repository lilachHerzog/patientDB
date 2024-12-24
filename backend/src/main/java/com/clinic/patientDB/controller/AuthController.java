//package com.clinic.patientDB.controller;
//
//import com.clinic.patientDB.security.*;
//import com.clinic.patientDB.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//
//@RestController
//@CrossOrigin
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenUtil;
//
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    AuthenticationManager am;
//
//    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) throws Exception {
//        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//        final String token = jwtTokenUtil.generateJwtToken(userDetails.getUsername(), authenticationRequest.getRole());
//        return ResponseEntity.ok(new AuthResponse(token));
//    }
//
//    @RequestMapping(value = "/user", method = RequestMethod.POST)
//    public ResponseEntity<?> createUser(@RequestBody AuthRequest userRequest) throws Exception {
//        String encodedPass = passwordEncoder.encode(userRequest.getPassword());
//        User user = User.UserBuilder.anUser().name(userRequest.getUsername())
//                .password(encodedPass).build();
//        userService.save(user);
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(userRequest.getUsername(), encodedPass, new ArrayList<>());
//        return ResponseEntity.ok(new AuthResponse(jwtTokenUtil.generateJwtToken(userDetails.getUsername(), userRequest.getRole()getRole)));
//    }
//
//    private void authenticate(String username, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//    }
//}
