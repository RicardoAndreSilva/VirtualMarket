package pt.virtualmarket.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.virtualmarket.userservice.entities.UserEntity;
import pt.virtualmarket.userservice.entities.UserResponse;
import pt.virtualmarket.userservice.services.UserService;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}")
    private ResponseEntity<UserResponse>  getUserDetails(@PathVariable("userId") int userId){
        UserResponse userResponse=userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
    @PostMapping("/users")
    private ResponseEntity<String> saveUser(@RequestBody UserEntity userEntity){
        userService.createUser(userEntity);
        return  ResponseEntity.status(HttpStatus.CREATED).body("user created");
    }
    @PutMapping("/users/{userId}")
    private ResponseEntity<String> updateUser(@PathVariable("userId") int userId, @RequestBody UserEntity userEntity){
        userService.updateUserById(userId,userEntity);
        return ResponseEntity.status(HttpStatus.OK).body("User updated");
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponses = userService.getAllUsers();
        return ResponseEntity.ok(userResponses);
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") int userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
