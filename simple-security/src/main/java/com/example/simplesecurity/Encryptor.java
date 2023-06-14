package com.example.simplesecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class Encryptor {

    public boolean encryptWithStandardPasswordEncoder(String password) {
        var secret = "aa";

        var passwordEncoder = new StandardPasswordEncoder(secret);
        var encodedPassword = passwordEncoder.encode(password);

        return passwordEncoder.matches(password, encodedPassword);
    }

    public boolean encryptWithBCryptPasswordEncoder(String password) {
        var passwordEncoder = new BCryptPasswordEncoder();
        var encodedPassword = passwordEncoder.encode(password);
        return passwordEncoder.matches(password, encodedPassword);
    }
}
