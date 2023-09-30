package auth;

import auth.configs.db.SupportedDatabases;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public interface IntegrateTestContainers {

    class Mongo {

        public static final MongoDBContainer container =
                new MongoDBContainer("mongo:7.0.1-jammy");

        @BeforeAll
        static void startContainer() {
            container.start();
        }

        @DynamicPropertySource
        static void propertiesRegistry(DynamicPropertyRegistry propertyRegistry) {
            propertyRegistry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
            propertyRegistry.add("db.selected", SupportedDatabases.MONGO::name);
        }

        @AfterAll
        static void stopContainer() {
            container.stop();
        }
    }
}
