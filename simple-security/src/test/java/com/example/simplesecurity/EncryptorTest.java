package com.example.simplesecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EncryptorTest {

    private Encryptor encryptor;

    @BeforeEach
    void setUp() {
        var userRepository = new UserRepository();
        encryptor = new Encryptor(userRepository);
    }

    @Nested
    @DisplayName("password 불일치")
    class PasswordMisMatches {

        @DisplayName("bcrypt(현재 알고리즘) encoded password면, false 리턴")
        @Test
        void test_0() {
            // given
            var username = "bcrypt-user1";
            var password = "bcrypt-password-2";

            // when
            var result = encryptor.matches(username, password);

            // then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result).isEqualTo(AuthenticationResult.FAIL);
        }

        @DisplayName("scrypt(이전 알고리즘) encoded password면, false 리턴")
        @Test
        void test_1() {
            // given
            var username = "scrypt-user1";
            var password = "scrypt-password-2";

            // when
            var result = encryptor.matches(username, password);

            // then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result).isEqualTo(AuthenticationResult.FAIL);
        }

        @DisplayName("sha256(이전 알고리즘) encoded password면, false 리턴")
        @Test
        void test_2() {
            // given
            var username = "sha256-user1";
            var password = "sha256-password-2";

            // when
            var result = encryptor.matches(username, password);

            // then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result).isEqualTo(AuthenticationResult.FAIL);
        }

        @DisplayName("argon2(이전 알고리즘) encoded password면 false 리턴")
        @Test
        void test_3() {
            // given
            var username = "argon2-user1";
            var password = "argon2-password-2";

            // when
            var result = encryptor.matches(username, password);

            // then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result).isEqualTo(AuthenticationResult.FAIL);
        }
    }

    @Nested
    @DisplayName("password 일치")
    class PasswordMatches {

        @DisplayName("bcrypt(현재 알고리즘) encoded password면 업그레이드하지 않고, true 리턴")
        @Test
        void test_0() {
            // given
            var username = "bcrypt-user1";
            var password = "bcrypt-password-1";

            // when
            var result = encryptor.matches(username, password);

            // then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result).isEqualTo(AuthenticationResult.SUCCESS);
        }

        @DisplayName("scrypt(이전 알고리즘) encoded password면 현재 알고리즘으로 업그레이드하고, true 리턴")
        @Test
        void test_1() {
            // given
            var username = "scrypt-user1";
            var password = "scrypt-password-1";

            // when
            var result = encryptor.matches(username, password);

            // then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result).isEqualTo(AuthenticationResult.ALGORITHM_UPGRADE);
        }

        @DisplayName("sha256(이전 알고리즘) encoded password면 현재 알고리즘으로 업그레이드하고, true 리턴")
        @Test
        void test_2() {
            // given
            var username = "sha256-user1";
            var password = "sha-256-password-1";

            // when
            var result = encryptor.matches(username, password);

            // then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result).isEqualTo(AuthenticationResult.ALGORITHM_UPGRADE);
        }

        @DisplayName("argon2(이전 알고리즘) encoded password면 현재 알고리즘으로 업그레이드하고, true 리턴")
        @Test
        void test_3() {
            // given
            var username = "argon2-user1";
            var password = "argon2-password-1";

            // when
            var result = encryptor.matches(username, password);

            // then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result).isEqualTo(AuthenticationResult.ALGORITHM_UPGRADE);
        }
    }

    @Nested
    @DisplayName("password format이 {algorithm id}password가 아닐 때")
    //  passwordEncoder.setDefaultPasswordEncoderForMatches(sha256PasswordEncoder); 필수 설정 검증
    class PasswordFormatMisMatches {

        @DisplayName("sha256(이전 알고리즘) encoded password면 현재 알고리즘으로 업그레이드하고, true 리턴")
        @Test
        void test_0() {
            // given
            var username = "sha256-user3";
            var password = "sha-256-password-3";

            // when
            var result = encryptor.matches(username, password);

            // then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result).isEqualTo(AuthenticationResult.ALGORITHM_UPGRADE);
        }
    }
}
