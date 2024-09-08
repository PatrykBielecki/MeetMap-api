// File: src/main/java/com/meetmap/model/Room.java
package com.meetmap.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomId;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Break circular reference on the parent side
    private List<User> users = new ArrayList<>();

    @PrePersist
    public void generateRoomId() {
        if (roomId == null || roomId.isEmpty()) {
            roomId = com.meetmap.util.RoomIdGenerator.generateRoomId();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
        user.setRoom(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setRoom(null);
    }
}
