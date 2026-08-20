package com.Aether.Journal.service;


import com.Aether.Journal.dto.UserRequestDto;
import com.Aether.Journal.dto.UserResponseDto;
import com.Aether.Journal.entity.User;
import com.Aether.Journal.exception.UserAlreadyExists;
import com.Aether.Journal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.AssertionErrors;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void createUserIfUserNotExistsTest() {

        // Arrange
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("Akash");
        requestDto.setPassword("ussr");

        when(userRepository.findByUsername("Akash"))
                .thenReturn(Optional.empty());

        when(encoder.encode("ussr"))
                .thenReturn("kjfherfubweweuf");

        User user = new User();
        user.setUsername("Akash");
        user.setPassword("kjfherfubweweuf");


        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponseDto responseDto = new UserResponseDto();

        when(modelMapper.map(user, UserResponseDto.class))
                .thenReturn(responseDto);


        // Act
        UserResponseDto result = userService.createUser(requestDto);


        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertSame(responseDto, result);

        verify(userRepository).findByUsername("Akash");
        verify(encoder).encode("ussr");
        verify(userRepository).save(any(User.class));
        verify(modelMapper).map(user, UserResponseDto.class);
    }

    @Test
    public void createUserIfUserAlreadyExists(){

        // Arrange
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("Akash");
        requestDto.setPassword("ussr");

        User existingUser = new User();
        existingUser.setUsername("Akash");

        when(userRepository.findByUsername("Akash"))
                .thenReturn(Optional.of(existingUser));

        // Act + Assert
        UserAlreadyExists exception = Assertions.assertThrows(
                UserAlreadyExists.class,
                () -> userService.createUser(requestDto)
        );

        // Assert exception message
        Assertions.assertEquals(
                "User already exists,Please use its credentials for login",
                exception.getMessage()
        );
    }

}
