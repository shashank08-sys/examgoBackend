package com.example.examgo.controller;

import com.example.examgo.dto.RegisterUserDto;
import com.example.examgo.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody RegisterUserDto dto) {
        return userService.registerUser(dto);
    }
}
