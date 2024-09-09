package com.meetmap.controller;

import com.meetmap.model.Room;
import com.meetmap.dto.UserRequest;
import com.meetmap.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody UserRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }
        try {
            Room room = roomService.createRoomWithUser(request.getUsername());
            return ResponseEntity.ok(room);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isPresent()) {
            return ResponseEntity.ok(room.get());
        } else {
            return ResponseEntity.status(404).body("Room not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/{roomId}/users")
    public ResponseEntity<?> addUserToRoom(@PathVariable String roomId, @RequestBody UserRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }
        try {
            Room room = roomService.addUserToRoomByUsername(roomId, request.getUsername());
            return ResponseEntity.ok(room);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
