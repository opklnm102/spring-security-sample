package com.example.simplesecurity;

import lombok.Getter;

@Getter
public enum AuthenticationResult {
    SUCCESS(true),
    FAIL(false),
    ALGORITHM_UPGRADE(true);

    private boolean success;

    AuthenticationResult(boolean success) {
        this.success = success;
    }
}
