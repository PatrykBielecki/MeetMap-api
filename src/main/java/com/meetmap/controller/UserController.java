package com.meetmap.controller;

import com.meetmap.model.User;
import com.meetmap.dto.UserRequest;
import com.meetmap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create a new user
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }
        try {
            User user = userService.createUser(request.getUsername());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID (with remaining time until deletion)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            User foundUser = user.get();

            // Calculate remaining time until deletion
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime deletionTime = foundUser.getDeletionTime();
            long minutesLeft = Duration.between(now, deletionTime).toMinutes();
            long secondsLeft = Duration.between(now, deletionTime).getSeconds() % 60;

            // Return the user details along with the remaining time
            return ResponseEntity.ok("User: " + foundUser.getUsername() + ", Time Left: "
                    + minutesLeft + " minutes, " + secondsLeft + " seconds");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Refresh user deletion timer
    @PostMapping("/{userId}/refresh")
    public ResponseEntity<?> refreshUserTimer(@PathVariable Long userId) {
        try {
            userService.refreshUserDeletionTimer(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
