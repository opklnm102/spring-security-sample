package com.example.simplesecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/*
use case
  - 각 서비스마다 password encoding 알고리즘이 다른 사용자 계정 통합
    - A: sha-256
    - B: Bcrypt
    - C: Scrypt
  - password encoding 알고리즘 변경
password 재설정으로 안내하기엔 UX가 좋지 않다
password를 migration하면서 각 서비스의 알고리즘을 함께 저장
password 검증시 알고리즘에 따라 password 일치 여부, 동일한 알고리즘으로 업그레이드하도록 구성
 */
@Slf4j
public class Encryptor {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public Encryptor(UserRepository userRepository) {
        var sha256PasswordEncoder = new MessageDigestPasswordEncoder("SHA-256");

        Map<String, PasswordEncoder> encoders = new HashMap();
        String encodingId = "bcrypt";
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("sha256", sha256PasswordEncoder);
        encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());

        var passwordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);

        // {id}password format이 아닐 때 사용할 encoder가 없으면
        // IllegalArgumentException: There is no PasswordEncoder mapped for the id 발생
        // default encoder를 설정하면 해당 encoder로 password를 인코딩
        passwordEncoder.setDefaultPasswordEncoderForMatches(sha256PasswordEncoder);

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public AuthenticationResult matches(String username, String rawPassword) {
        var user = userRepository.findByUsername(username);

        if (!passwordEncoder.matches(rawPassword, user.encodedPassword())) {
            return AuthenticationResult.FAIL;
        }

        // password가 일치할 경우 upgrade
        return upgradePassword(rawPassword, user);
    }

    private AuthenticationResult upgradePassword(String rawPassword, User user) {
        // 업그레이드가 불필요하면 skip
        if (!passwordEncoder.upgradeEncoding(user.encodedPassword())) {
            return AuthenticationResult.SUCCESS;
        }

        log.info("need upgrade");
        userRepository.save(new User(user.username(), passwordEncoder.encode(rawPassword)));

        return AuthenticationResult.ALGORITHM_UPGRADE;
    }
}
