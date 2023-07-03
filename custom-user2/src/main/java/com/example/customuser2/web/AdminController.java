package com.example.customuser2.web;

import com.example.customuser2.domain.type.AdminAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/admin")
    @AdminAuthorize
    public String endpoint() {
        return "admin";
    }
}
