package com.damianroszczyk.events.controllers;

import com.damianroszczyk.events.models.Event;
import com.damianroszczyk.events.models.User;
import com.damianroszczyk.events.repositories.EventRepository;
import com.damianroszczyk.events.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@CrossOrigin
@RestController
public class EventController {

    private EventRepository eventRepository;
    private UserRepository userRepository;

    @Autowired
    public EventController(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/events")
    public ResponseEntity<Set<Event>> getUserEvents(@AuthenticationPrincipal User principal) {
        try {
            Optional<User> user = userRepository.findByEmail(principal.getEmail());
            if (user.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(user.get().getEvents());
            }
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/api/events")
    public ResponseEntity<Event> addEvent(@RequestBody Event event, @AuthenticationPrincipal User principal) {
        try {
            Optional<User> user = userRepository.findByEmail(principal.getEmail());
            if (user.isPresent()) {
                event.setUser(user.get());
                eventRepository.save(event);
                return ResponseEntity.status(HttpStatus.OK).body(event);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @DeleteMapping("/api/events/{id}")
    public ResponseEntity deleteEvent(@PathVariable long id) {
        Optional<Event> e = eventRepository.findById(id);
        if(e.isPresent()) {
            eventRepository.delete(e.get());
            return ResponseEntity.status(200).body(null);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }
}
