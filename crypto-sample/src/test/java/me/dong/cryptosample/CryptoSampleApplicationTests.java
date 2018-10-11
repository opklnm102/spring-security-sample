package me.dong.cryptosample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CryptoSampleApplicationTests {

	@Test
	public void contextLoads() {
	}

    @Test
    public void test() throws Exception {
        // given
        String secret = "aa";
        String password = "password";

        // BCryptPasswordEncoder가  더 좋다
        // deprecated로 DelegatingPasswordEncoder 이걸 사용하라네...
        StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder(secret);


        // when
        String encodedPassword = passwordEncoder.encode(password);  // salt를 내부에서 만든다
        log.info("encodedPassword : {}", encodedPassword);

        boolean result = passwordEncoder.matches(password, encodedPassword);

        // then
        assertThat(result).isTrue();
    }
}
