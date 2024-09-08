// File: src/main/java/com/meetmap/repository/UserRepository.java
package com.meetmap.repository;

import com.meetmap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
