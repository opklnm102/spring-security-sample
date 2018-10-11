package me.dong.springboot1cryptosample;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by ethan.kim on 2018. 5. 27..
 */
public class ShaPasswordEncoder implements PasswordEncoder {

    private org.springframework.security.authentication.encoding.ShaPasswordEncoder passwordEncoder;

    private Object salt = null;

    public ShaPasswordEncoder() {
        this.passwordEncoder = new org.springframework.security.authentication.encoding.ShaPasswordEncoder();
    }

    public ShaPasswordEncoder(int sha) {
        this.passwordEncoder = new org.springframework.security.authentication.encoding.ShaPasswordEncoder(sha);
    }

    public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
        passwordEncoder.setEncodeHashAsBase64(encodeHashAsBase64);
    }

    public void setSalt(Object salt) {
        this.salt = salt;
    }


    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encodePassword(rawPassword.toString(), salt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.isPasswordValid(encodedPassword, rawPassword.toString(), salt);
    }
}
