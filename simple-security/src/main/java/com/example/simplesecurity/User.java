package com.example.simplesecurity;

public record User(
        String username,
        String encodedPassword) {
}
