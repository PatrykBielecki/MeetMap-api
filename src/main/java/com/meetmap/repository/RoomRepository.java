// File: src/main/java/com/meetmap/repository/RoomRepository.java
package com.meetmap.repository;

import com.meetmap.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomId(String roomId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Room r LEFT JOIN r.users u WHERE r.id = :roomId")
    boolean checkIfRoomByIdHasUsersInIt(@Param("roomId") Long roomId);
}
