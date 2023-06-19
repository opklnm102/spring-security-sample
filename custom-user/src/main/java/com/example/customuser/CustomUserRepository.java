package com.example.customuser;

import java.util.Optional;

public interface CustomUserRepository {
    Optional<CustomUser> findCustomUserByEmail(String email);
}
