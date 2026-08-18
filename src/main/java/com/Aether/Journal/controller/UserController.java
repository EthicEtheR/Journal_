package com.Aether.Journal.controller;

import com.Aether.Journal.dto.UserRequestDto;
import com.Aether.Journal.dto.UserResponseDto;
import com.Aether.Journal.service.UserService;
import lombok.NonNull;
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



    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUser(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUser());

    }

    //if user want it can delete his User/principals but if admin want to remove any User ,they should have id for this
    @DeleteMapping
    public ResponseEntity<String> deleteUser(){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(userService.deleteUser());

    }

    @PutMapping
    public ResponseEntity<@NonNull UserResponseDto> updateUser(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateExistingUser(userRequestDto));
    }


}
