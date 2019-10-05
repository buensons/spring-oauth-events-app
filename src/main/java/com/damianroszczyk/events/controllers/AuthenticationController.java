//package com.damianroszczyk.events.controllers;
//
//import com.damianroszczyk.events.models.User;
//import com.damianroszczyk.events.repositories.UserRepository;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.Date;
//
//@RestController
//public class AuthenticationController {
//
//    @Value("${security.oauth2.client.clientId}")
//    private String googleClientId;
//
//    @Value("${security.oauth2.client.clientSecret}")
//    private String secretKey;
//
//    private GoogleIdTokenVerifier verifier;
//    private UserRepository userRepository;
//
//    @Autowired
//    public AuthenticationController(UserRepository userRepository) throws GeneralSecurityException, IOException {
//        JsonFactory jsonFactory = new JacksonFactory();
//        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
//        this.verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
//                .setAudience(Collections.singletonList(googleClientId))
//                .build();
//        this.userRepository = userRepository;
//    }
//
//    @PostMapping("/googleLogin")
//    public ResponseEntity<String> login(@RequestBody String googleToken) {
//        try {
//
//            GoogleIdToken idToken = verifier.verify(googleToken);
//
//            if (idToken != null) {
//                GoogleIdToken.Payload payload = idToken.getPayload();
//                String userId = payload.getSubject();
//
//                if(userRepository.findByPrincipalId(userId).isEmpty()) {
//                    var newUser = new User(payload.getEmail(), (String) payload.get("name"));
//                    newUser.setPrincipalId(userId);
//                    newUser.setPhoto((String)payload.get("picture"));
//                    userRepository.save(newUser);
//                }
//
//                String token = Jwts.builder()
//                        .setSubject(payload.getSubject())
//                        .claim("roles","user")
//                        .setIssuedAt(new Date(System.currentTimeMillis()))
//                        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
//                        .signWith(SignatureAlgorithm.HS512, secretKey)
//                        .compact();
//
//                return ResponseEntity.status(HttpStatus.OK).body(token);
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } catch(Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//}
