package com.jberdeja.idm_authorization;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.wait.strategy.Wait;

@Testcontainers
@SpringBootTest 
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ContainerConfig {
    @Autowired
    private MongoTemplate mongoTemplate;

    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0"));
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @BeforeAll
    static void setUpAll() {
        mongoDBContainer.start();
        postgreSQLContainer.start();
        postgreSQLContainer.waitingFor(Wait.forListeningPort());  // Asegurarse de que PostgreSQL esté listo
    }

    @BeforeEach
    void setUpByMethod() {
        mongoTemplate.getDb().drop();
    }

    @BeforeEach
    void setDownMethod() {
        if (!mongoDBContainer.isRunning()) {
            mongoDBContainer.start();
        }
        if (!postgreSQLContainer.isRunning()) {
            postgreSQLContainer.start();
        }
    }
    @DynamicPropertySource
    static void configureMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    } 


    @AfterAll
    static void tearDown() {
        // Detener contenedores para liberar recursos
        if (mongoDBContainer != null) {
            mongoDBContainer.stop();
        }
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }  

}
