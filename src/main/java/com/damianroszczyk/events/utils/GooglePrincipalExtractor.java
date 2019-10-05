package com.damianroszczyk.events.utils;

import com.damianroszczyk.events.models.User;
import com.damianroszczyk.events.repositories.UserRepository;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.*;

public class GooglePrincipalExtractor implements PrincipalExtractor {

    private UserRepository userRepository;

    public GooglePrincipalExtractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {

        Optional<User> user = userRepository.findByEmail((String)map.get("email"));
        User newUser;
        if(!user.isPresent()) {
            newUser = new User();
            newUser.setUsername((String)map.get("name"));
            newUser.setEmail((String)map.get("email"));
            newUser.setPrincipalId((String)map.get("id"));
            newUser.setPhoto((String) map.get("picture"));
        } else {
            return user.get();
        }
        userRepository.save(newUser);
        return newUser;
    }
}
