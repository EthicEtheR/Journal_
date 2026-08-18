package com.Aether.Journal.controller;

import com.Aether.Journal.dto.UserRequestDto;
import com.Aether.Journal.dto.UserResponseDto;
import com.Aether.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto){

        if(requestDto.getUsername().isEmpty())
            throw new UsernameNotFoundException("username cannot be empty");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(requestDto));
    }
}
