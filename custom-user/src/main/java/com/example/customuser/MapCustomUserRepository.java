package com.example.customuser;

import java.util.Map;
import java.util.Optional;

public class MapCustomUserRepository implements CustomUserRepository {

    private final Map<String, CustomUser> emailToCustomUser;

    public MapCustomUserRepository(Map<String, CustomUser> emailToCustomUser) {
        this.emailToCustomUser = emailToCustomUser;
    }

    @Override
    public Optional<CustomUser> findCustomUserByEmail(String email) {
        return Optional.ofNullable(emailToCustomUser.get(email));
    }
}
