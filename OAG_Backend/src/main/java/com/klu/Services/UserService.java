package com.klu.Services;

import com.klu.Interfaces.IUserRepository;
import com.klu.Models.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Get user by ID
    public Optional<Users> getUserById(int id) {
        return userRepository.getUserById(id);
    }

    // Get all users
    public List<Users> getAllUsers() {
        return userRepository.getAllUsers();
    }

    // Add new user
    public Users addUser(Users user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.addUser(user);
        return user;
    }

    // Update user
    public Users updateUser(Users user) {
        userRepository.updateUser(user);
        return user;
    }

    // Delete user
    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    // Authenticate user
    public Optional<Users> authenticate(String email, String rawPassword) {
        Optional<Users> userOpt = Optional.ofNullable(userRepository.findByEmail(email));

        if (userOpt.isPresent() && passwordEncoder.matches(rawPassword, userOpt.get().getPassword())) {
            return userOpt;
        } else {
            return Optional.empty();
        }
    }
}
