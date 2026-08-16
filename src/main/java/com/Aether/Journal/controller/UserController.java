package com.Aether.Journal.controller;

import com.Aether.Journal.dto.UserRequestDto;
import com.Aether.Journal.dto.UserResponseDto;
import com.Aether.Journal.entity.User;
import com.Aether.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUser(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUser());

    }


}
