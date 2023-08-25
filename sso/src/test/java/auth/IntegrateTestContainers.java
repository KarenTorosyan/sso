package auth;

import auth.configs.db.SupportedDatabases;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public interface IntegrateTestContainers {

    class Mongo {
        @DynamicPropertySource
        static void propertiesRegistry(DynamicPropertyRegistry propertyRegistry) {
            MongoDBContainer container = new MongoDBContainer("mongo:7.0-rc-jammy");
            container.start();
            propertyRegistry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
            propertyRegistry.add("db.selected", SupportedDatabases.MONGO::name);
        }
    }
}
