package pt.virtualmarket.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.virtualmarket.userservice.entities.UserEntity;
import pt.virtualmarket.userservice.entities.UserResponse;
import pt.virtualmarket.userservice.exceptions.HttpException;
import pt.virtualmarket.userservice.services.UserService;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserDetails(@PathVariable("userId") int userId) {
        try {
            UserResponse userResponse = userService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        } catch (HttpException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode())).build();
        }
    }
    @PostMapping("/users")
    public ResponseEntity<String> saveUser(@RequestBody UserEntity user) {
        try {
            userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created");
        } catch (HttpException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode())).build();
        }
    }
    @PutMapping("/users/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") int userId,
        @RequestBody UserEntity updatedUser) {
        try {
            userService.updateUserById(userId, updatedUser);
            return ResponseEntity.status(HttpStatus.OK).body("User updated");
        } catch (HttpException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode())).build();
        }
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try {
            List<UserResponse> userResponses = userService.getAllUsers();
            return ResponseEntity.ok(userResponses);
        } catch (HttpException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode())).build();
        }
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") int userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (HttpException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode())).build();
        }
    }
}











