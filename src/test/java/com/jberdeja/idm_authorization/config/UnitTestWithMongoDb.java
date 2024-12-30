package com.jberdeja.idm_authorization.config;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) 
public class UnitTestWithMongoDb {
    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");
    @Autowired
    private MongoTemplate mongoTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.waitingFor(Wait.forListeningPort());
        mongoDBContainer.withStartupTimeout(Duration.ofSeconds(1));
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    protected
    void setUpByMethod() {
        mongoTemplate.getDb().drop();
    } 
}
