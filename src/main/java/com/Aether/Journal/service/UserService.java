package com.Aether.Journal.service;

import com.Aether.Journal.dto.UserRequestDto;
import com.Aether.Journal.dto.UserResponseDto;
import com.Aether.Journal.entity.User;
import com.Aether.Journal.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user=new User(requestDto.getUsername(),requestDto.getPassword());

        user= userRepository.save(user);


      return modelMapper.map(user,UserResponseDto.class);

    }

    public  List<UserResponseDto> getAllUser() {
        List<User> users= userRepository.findAll();

        return users.stream()
                .map(user->modelMapper.map(user,UserResponseDto.class))
                .toList();
    }
}
