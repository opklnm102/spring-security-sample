package com.example.customuser2.web;

import com.example.customuser2.domain.CustomUserDetails;
import com.example.customuser2.domain.type.UserAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    @UserAuthorize
    public UserDto user(@AuthenticationPrincipal CustomUserDetails user) {
        return new UserDto(user);
    }
}
