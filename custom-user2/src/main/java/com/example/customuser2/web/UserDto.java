package com.example.customuser2.web;

import com.example.customuser2.domain.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserDto(String username,
                      String password,
                      boolean accountNonExpired,
                      boolean accountNonLocked,
                      boolean credentialsNonExpired,
                      boolean enabled,
                      String userGrade,
                      Collection<String> authorities) {

    public UserDto(CustomUserDetails user) {
        this(user.getUsername(),
                user.getPassword(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getUserGrade().getName(),
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());
    }
}
