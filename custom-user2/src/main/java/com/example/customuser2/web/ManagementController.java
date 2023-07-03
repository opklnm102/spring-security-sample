package com.example.customuser2.web;

import com.example.customuser2.domain.type.StaffAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagementController {

    @GetMapping("/management")
    @StaffAuthorize
    public String endpoint() {
        return "management";
    }
}
