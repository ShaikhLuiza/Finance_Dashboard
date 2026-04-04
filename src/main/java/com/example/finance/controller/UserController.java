package com.example.finance.controller;

import com.example.finance.model.Role;
import com.example.finance.model.User;
import com.example.finance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 2. Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
    // 3. Toggle active/inactive status
    @PostMapping("/{id}/status")
    public ResponseEntity<User> toggleStatus(@PathVariable Long id, @RequestParam boolean active) {
        return ResponseEntity.ok(userService.toggleUserStatus(id, active));
    }

    // 4. Change a user's role
    @PostMapping("/{id}/role")
    public ResponseEntity<User> assignRole(@PathVariable Long id, @RequestParam Role role) {
        return ResponseEntity.ok(userService.assignRole(id, role));
    }
}