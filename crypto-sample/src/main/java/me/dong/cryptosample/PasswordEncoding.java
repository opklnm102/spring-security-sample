package me.dong.cryptosample;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by ethan.kim on 2018. 5. 26..
 */
public class PasswordEncoding implements PasswordEncoder {

    // 다른 암호화 클래스를 주입 받을 수 있다

    private PasswordEncoder passwordEncoder;

    public PasswordEncoding() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public PasswordEncoding(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
