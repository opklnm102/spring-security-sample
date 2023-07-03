package com.example.customuser2.domain.type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserGrade {
    BRONZE("bronze"),
    SILVER("silver"),
    GOLD("gold");

    private final String name;

    UserGrade(String name) {
        this.name = name;
    }

    public static UserGrade get(String name) {
        return Arrays.stream(values())
                .filter(userGrade -> userGrade.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(name + " does not support"));
    }
}
