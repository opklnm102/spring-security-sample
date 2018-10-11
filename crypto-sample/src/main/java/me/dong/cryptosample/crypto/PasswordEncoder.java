package me.dong.cryptosample.crypto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ethan.kim on 2018. 5. 26..
 */
@Slf4j
public class PasswordEncoder {
    // TbEncryptor

    /*
    암호화 모듈을 만드는데
    crypto service가 있으면 인터페이스를 만들고
    각 구현체마다 가지고 있는 암호화 function이 다르다
    그리고 generic 구현체에 사용할 암호화 function과 함께 넘기면 생성해주는 그런 코드...?
     */

    public void encriptWithStandardPasswordEncoder() {
        String secret = "aa";
        String password = "password";

        StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder(secret);

        String encodedPassword = passwordEncoder.encode(password);  // salt를 내부에서 만든다
        log.info("encodedPassword : {}", encodedPassword);


        boolean result = passwordEncoder.matches(password, encodedPassword);
    }


    public void encriptWithBCryptPasswordEncoder() {
        String rawPassword = "";

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encodedPassword = passwordEncoder.encode(rawPassword);

        boolean result = passwordEncoder.matches(rawPassword, encodedPassword);
    }


}
