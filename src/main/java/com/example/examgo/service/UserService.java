package com.example.examgo.service;

import com.example.examgo.dto.RegisterUserDto;
import com.example.examgo.model.User;
import com.example.examgo.repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // In-memory store for reset tokens (for demo; use DB or cache in production)
    private final ConcurrentHashMap<String, String> resetTokens = new ConcurrentHashMap<>();

    @Autowired
    private UserMailService userMailService;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(RegisterUserDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return "Passwords do not match";
        }
        Optional<User> existing = userRepository.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            return "Email already registered";
        }
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(hashedPassword); // Store hashed password
        userRepository.save(user);
        return "User registered successfully";
    }

    public String initiatePasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        System.out.println("Initiating password reset for email: " + email);
        if (userOpt.isEmpty()) {
            System.out.println("No user found with email: " + email);
            return "If the email exists, a reset link will be sent.";
        }
        String token = UUID.randomUUID().toString();
        resetTokens.put(token, email);
        String resetLink = "http://localhost:5173/reset-password?token=" + token;
        userMailService.sendResetLink(email, resetLink);
        return "Password reset link sent to your email.";
    }

    public String resetPassword(String token, String newPassword, String confirmPassword) {
        String email = resetTokens.get(token);
        if (email == null) {
            return "Invalid or expired token.";
        }
        if (!newPassword.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "User not found.";
        }
        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetTokens.remove(token);
        return "Password reset successful.";
    }
}
