package com.klu.Controllers;

import com.klu.Models.Users;
import com.klu.Services.UserService;
import com.klu.DTO.LoginRequest;
import com.klu.DTO.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable int id) {
        Optional<Users> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        Users user = new Users();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword(userDto.getPassword());

        Users createdUser = userService.addUser(user);

        UserDto responseDto = new UserDto();
        responseDto.setId(createdUser.getId());
        responseDto.setName(createdUser.getName());
        responseDto.setEmail(createdUser.getEmail());
        responseDto.setPhone(createdUser.getPhone());

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Users> userOpt = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("Invalid email or password");

        Users user = userOpt.get();
        return ResponseEntity.ok(new Object() {
            public final String message = "Login successful";
            public final Object userDetails = new Object() {
                public final int id = user.getId();
                public final String name = user.getName();
                public final String email = user.getEmail();
                public final String phone = user.getPhone();
            };
        });
    }
}
