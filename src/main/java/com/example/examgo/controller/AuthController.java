package com.example.examgo.controller;

import com.example.examgo.dto.RegisterUserDto;
import com.example.examgo.model.User;
import com.example.examgo.repository.UserRepository;
import com.example.examgo.service.UserService;
import com.example.examgo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        System.out.println("Login attempt for email: " + user.getEmail());
        String email = user.getEmail();
        String rawPassword = user.getPassword();
        Optional<User> dbUserOpt = userRepository.findByEmail(email);
        if (dbUserOpt.isPresent()) {
            User dbUser = dbUserOpt.get();
            if (passwordEncoder.matches(rawPassword, dbUser.getPassword())) {
                return jwtUtil.generateToken(email);
            }
        }
        throw new RuntimeException("Invalid credentials");
    }
// this is the post mapping for forgot password, it will receivthe email from the and call the userService to initiate the password reset process. The userService will check if the email exists in the database, generate a reset token, and send a reset link to the user's email. The reset link will contain the token as a query parameter, which can be used to verify the user's identity when they click on the link.
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> request) {
        logger.info("/auth/forgot-password endpoint hit with email: {}", request.get("email"));
        String email = request.get("email");
        return userService.initiatePasswordReset(email);
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String, Object> dto) {
        // Assuming RegisterUserDto fields are present in the request body
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setFirstName((String) dto.get("firstName"));
        registerUserDto.setLastName((String) dto.get("lastName"));
        registerUserDto.setEmail((String) dto.get("email"));
        registerUserDto.setPassword((String) dto.get("password"));
        registerUserDto.setConfirmPassword((String) dto.get("confirmPassword"));
        return userService.registerUser(registerUserDto);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");
        return userService.resetPassword(token, newPassword, confirmPassword);
    }
}