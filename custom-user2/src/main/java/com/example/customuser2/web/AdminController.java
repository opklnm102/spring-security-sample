package com.example.customuser2.web;

import com.example.customuser2.domain.type.AdminAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/admin/{id}")
    @AdminAuthorize
    public String endpoint(@PathVariable("id") Long id,
                           @RequestParam("userName") String userName) {
        return "admin";
    }
}
