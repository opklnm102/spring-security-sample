package me.dong.cryptosample;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * Created by ethan.kim on 2018. 5. 26..
 */
public class ShaPasswordEncoder implements PasswordEncoder {

    private PasswordEncoder passwordEncoder;

    public ShaPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ShaPasswordEncoder() {
        this.passwordEncoder = new StandardPasswordEncoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
