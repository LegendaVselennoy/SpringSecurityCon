package org.example.dockertest;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractionBaseTest {

    static final MySQLContainer MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
                .withDatabaseName("test")
                .withUsername("root")
                .withPassword("pass");

        MYSQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void configureMySQL(DynamicPropertyRegistry registry) {
        registry.add("spring.datasourse.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasourse.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasourse.password", MYSQL_CONTAINER::getPassword);
    }
}
