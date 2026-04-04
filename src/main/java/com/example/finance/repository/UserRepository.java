package com.example.finance.repository;

import com.example.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Boot automatically builds the SQL to find a user by username!
    Optional<User> findByUsername(String username);
}