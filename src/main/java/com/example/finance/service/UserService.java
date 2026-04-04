package com.example.finance.service;

import com.example.finance.model.Role;
import com.example.finance.model.User;
import com.example.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create or update a user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Find by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Fulfills Requirement #1: Managing active/inactive status
    public User toggleUserStatus(Long id, boolean status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(status);
        return userRepository.save(user);
    }

    // Assign a new role to a user
    public User assignRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return userRepository.save(user);
    }
}