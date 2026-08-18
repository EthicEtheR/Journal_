package com.Aether.Journal.service;

import com.Aether.Journal.entity.User;
import com.Aether.Journal.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      User user =userRepository.findByUsername(username)
              .orElseThrow(()->new UsernameNotFoundException("User not found"));

      UserDetails userDetails=new UserDetails() {
          @Override
          public Collection<? extends GrantedAuthority> getAuthorities() {
              return user.getRoles().stream()
                      .map(role->new SimpleGrantedAuthority("ROLE_"+role))
                      .collect(Collectors.toSet());
          }

          @Override
          public @Nullable String getPassword() {
              return user.getPassword();
          }

          @Override
          public String getUsername() {
              return user.getUsername();
          }
      };


        return userDetails;
    }
}
