package com.meetmap.service;

import com.meetmap.dto.RoomRequest;
import com.meetmap.dto.UserRequest;
import com.meetmap.model.Room;
import com.meetmap.model.User;
import com.meetmap.repository.RoomRepository;
import com.meetmap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    private final ConcurrentHashMap<Long, ScheduledFuture<?>> userTimers = new ConcurrentHashMap<>();

    public User createUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setDeletionTime(LocalDateTime.now().plusMinutes(5));  // Set deletion time

        user = userRepository.save(user);  // Save the user

        startUserDeletionTimer(user.getId());  // Start the deletion timer
        return user;
    }

    public RoomRequest updateUserLocation(Long userId, Double longitude, Double latitude) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLongitude(longitude);
        user.setLatitude(latitude);
        userRepository.save(user);

        Room room = user.getRoom();
        if (room == null) {
            throw new RuntimeException("User is not assigned to any room");
        }

        // Convert the Room and Users into DTOs
        List<UserRequest> userDTOs = room.getUsers().stream()
                .map(u -> new UserRequest(u.getId(), u.getUsername(), u.getLongitude(), u.getLatitude()))
                .collect(Collectors.toList());

        return new RoomRequest(room.getId(), room.getRoomId(), userDTOs);
    }

    public void refreshUserDeletionTimer(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Reset the deletion time to 5 minutes from now
            user.setDeletionTime(LocalDateTime.now().plusMinutes(5));
            userRepository.save(user);

            cancelUserDeletionTimer(userId);
            startUserDeletionTimer(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long userId) {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the user's room
        Room room = user.getRoom();

        // Remove the user from the repository
        userRepository.deleteById(userId);
        cancelUserDeletionTimer(userId);

        // If the user had a room, try to delete the room (RoomService checks if it's empty)
        if (room != null) {
            boolean hasUsers = roomRepository.checkIfRoomByIdHasUsersInIt(room.getId());

            if (!hasUsers) {
                roomRepository.deleteById(room.getId());
            }
        }
    }

    private void startUserDeletionTimer(Long userId) {
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(() -> deleteUser(userId),
                new java.util.Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));

        userTimers.put(userId, scheduledFuture);
    }

    private void cancelUserDeletionTimer(Long userId) {
        ScheduledFuture<?> scheduledFuture = userTimers.remove(userId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }
}
