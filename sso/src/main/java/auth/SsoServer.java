package auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ConfigurationPropertiesScan
@PropertySource({"classpath:application.env"})
public class SsoServer {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(SsoServer.class)
                .run(args);
    }
}