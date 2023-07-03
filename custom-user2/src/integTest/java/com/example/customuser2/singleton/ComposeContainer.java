package com.example.customuser2.singleton;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

/**
 * Singleton containers
 * https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers
 */
@ActiveProfiles("test")
public abstract class ComposeContainer {

    // shared container - shared between test modules
    private static final DockerComposeContainer<?> CONTAINERS;

    static {
        CONTAINERS = new DockerComposeContainer<>(new File("docker-compose-test.yml"))
                .withExposedService("mysql_1", 3306, Wait.forListeningPort());
        CONTAINERS.start();
    }

    @DynamicPropertySource
    public static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_HOST", () -> CONTAINERS.getServiceHost("mysql_1", 3306));
        registry.add("DB_PORT", () -> CONTAINERS.getServicePort("mysql_1", 3306));
    }
}
