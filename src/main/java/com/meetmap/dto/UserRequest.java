package com.meetmap.dto;

public class UserRequest {
    private Long id;
    private String username;
    private Double longitude;
    private Double latitude;

    public UserRequest(Long id, String username, Double longitude, Double latitude) {
        this.id = id;
        this.username = username;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
