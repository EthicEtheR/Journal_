package com.Aether.Journal.service;

import com.Aether.Journal.dto.UserRequestDto;
import com.Aether.Journal.dto.UserResponseDto;
import com.Aether.Journal.entity.User;
import com.Aether.Journal.enums.RoleType;
import com.Aether.Journal.exception.EmptyCredentialsException;
import com.Aether.Journal.exception.UserAlreadyExists;
import com.Aether.Journal.repository.UserRepository;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder encoder;

    public UserResponseDto createUser(UserRequestDto requestDto) {

       User exists=userRepository.findByUsername(requestDto.getUsername())
               .orElse(null);

        User user=new User();

       if(exists==null) {

           user.setUsername(requestDto.getUsername());
           if(!requestDto.getPassword().isEmpty())
               user.setPassword(encoder.encode(requestDto.getPassword()));
           else throw  new EmptyCredentialsException("Empty Credentials is not allowed");
           user.setRoles(Set.of(RoleType.USER));

           user = userRepository.save(user);
       }
       else{
           throw new UserAlreadyExists("User already exists,Please use its credentials for login");
       }



      return modelMapper.map(user,UserResponseDto.class);

    }

    public  List<UserResponseDto> getAllUser() {
        List<User> users= userRepository.findAll();

        return users.stream()
                .map(user->modelMapper.map(user,UserResponseDto.class))
                .toList();
    }

    public  String deleteUser() {

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();


            String username = authentication.getName();


       User user=userRepository.findByUsername(username)
                .orElse(null);

        if(user==null){
           throw new UsernameNotFoundException("User not found,maybe it was deleted");
        }
        userRepository.deleteById(user.getId());

        return "Successfully deleted";


    }


    public  @NonNull UserResponseDto updateExistingUser(UserRequestDto userRequestDto) {
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();



            User user=userRepository.findByUsername(username)
                    .orElseThrow(()->new UsernameNotFoundException(
                            "User not exists by username: "+username));

            if(!userRequestDto.getUsername().isEmpty() &&
                    !userRequestDto.getPassword().isEmpty()) {
                user.setUsername(userRequestDto.getUsername());
                user.setPassword(Objects.requireNonNull(encoder.encode(userRequestDto.getPassword())));
                user=userRepository.save(user);
                return modelMapper.map(user,UserResponseDto.class);
            }

        throw new EmptyCredentialsException("username or password or both is empty");
    }
}
