package com.example.customuser2.repository;

import com.example.customuser2.singleton.JpaConfiguration;
import com.example.customuser2.entity.user.Privilege;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@TestPropertySource(properties = {
        "spring.test.database.replace=none"
})
@ActiveProfiles("test")
@Import(JpaConfiguration.class)
class PrivilegeRepositoryTest {

    @Container
    static MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withUsername("root")
            .withPassword("password")
            .withInitScript("data/init.sql");

    @DynamicPropertySource
    static void dynamicProperty(DynamicPropertyRegistry registry) {
        registry.add("DB_HOST", MYSQL::getHost);
        registry.add("DB_PORT", MYSQL::getFirstMappedPort);
    }

    @Autowired
    private PrivilegeRepository sut;

    @Test
    @Sql("classpath:/data/test/seed-data.sql")
    void testGetAllPrivileges() {

        // when
        var result = sut.findAll();

        // then
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    void testSavePrivileges() {
        // given
        var privilegeName = "READ_PRIVILEGE";

        // when
        var result = sut.save(new Privilege(privilegeName));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(privilegeName);
    }
}
