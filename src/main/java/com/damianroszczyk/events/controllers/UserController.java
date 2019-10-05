package com.damianroszczyk.events.controllers;

import com.damianroszczyk.events.models.User;
import com.damianroszczyk.events.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/users/{username}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String username) {
        Optional<User> user = userRepository.findByEmail(username);
        return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/api/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    @PostMapping("/api/users")
    public ResponseEntity addUser(@RequestBody User user) {
        try {
            userRepository.save(user);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.findByEmail(user.getEmail()));
    }

    @GetMapping("/api/me")
    public ResponseEntity<User> getPrincipal(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
