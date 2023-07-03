package com.example.customuser2.domain.type;

import lombok.Getter;

@Getter
public enum UseYN {
    Y(true),
    N(false);

    private final boolean status;

    UseYN(boolean status) {
        this.status = status;
    }
}
