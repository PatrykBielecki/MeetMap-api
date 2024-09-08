package com.meetmap.dto;

import java.util.List;

public class RoomRequest {
    private Long id;
    private String roomId;
    private List<UserRequest> users;

    public RoomRequest(Long id, String roomId, List<UserRequest> users) {
        this.id = id;
        this.roomId = roomId;
        this.users = users;
    }

    // Getters and setters
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

    public List<UserRequest> getUsers() {
        return users;
    }

    public void setUsers(List<UserRequest> users) {
        this.users = users;
    }
}
