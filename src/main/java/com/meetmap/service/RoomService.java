package com.meetmap.service;

import com.meetmap.model.Room;
import com.meetmap.model.User;
import com.meetmap.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserService userService;

    public Room createRoomWithUser(String username) {
        User user = userService.createUser(username);

        Room room = new Room();
        room.setRoomId(generateRandomRoomId());
        room = roomRepository.save(room);
        user.setRoom(room);

        room.addUser(user);  // Add the user to the room
        return roomRepository.save(room);  // Save the room with the user
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public Room addUserToRoomByUsername(String roomId, String username) {
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            throw new RuntimeException("Room not found");
        }

        User user = userService.createUser(username);
        user.setRoom(room);

        room.addUser(user);
        return roomRepository.save(room);
    }

    private String generateRandomRoomId() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
