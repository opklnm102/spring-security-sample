package com.example.customuser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class CustomUser {

    private final long id;
    private final String email;

    @JsonIgnore
    private final String password;

    @JsonCreator
    public CustomUser(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
