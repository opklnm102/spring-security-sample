package com.example.customuser2.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

class ComposeExtension implements BeforeAllCallback, AfterAllCallback {

    // shared container - shared between test modules
    private DockerComposeContainer<?> container;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        container = new DockerComposeContainer<>(new File("docker-compose-test.yml"))
                .withExposedService("mysql_1", 3306, Wait.forListeningPort());
        container.start();

        System.setProperty("DB_HOST", container.getServiceHost("mysql_1", 3306));
        System.setProperty("DB_PORT", String.valueOf(container.getServicePort("mysql_1", 3306)));
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // do nothing, Testcontainers handles container shutdown
    }
}
