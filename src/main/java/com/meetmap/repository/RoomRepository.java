// File: src/main/java/com/meetmap/repository/RoomRepository.java
package com.meetmap.repository;

import com.meetmap.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomId(String roomId);
}
