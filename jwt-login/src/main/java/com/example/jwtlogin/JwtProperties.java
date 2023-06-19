package com.example.jwtlogin;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties("jwt")
@Validated
public record JwtProperties(@NotNull RSAPublicKey publicKey,
                            @NotNull RSAPrivateKey privateKey) {
}
