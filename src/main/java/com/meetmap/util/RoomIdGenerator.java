// File: src/main/java/com/meetmap/util/RoomIdGenerator.java
package com.meetmap.util;

import java.util.Random;

public class RoomIdGenerator {

    private static final int ROOM_ID_LENGTH = 6;
    private static final Random RANDOM = new Random();

    public static String generateRoomId() {
        StringBuilder roomId = new StringBuilder();
        for (int i = 0; i < ROOM_ID_LENGTH; i++) {
            roomId.append(RANDOM.nextInt(10)); // Generate a 6-digit room ID
        }
        return roomId.toString();
    }
}
