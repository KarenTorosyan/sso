package auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileBasedSecretsEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String SECRET_FILE_SUFFIX = "_SECRET_FILE";
    private static final String SPRING_BOOT_PROPERTIES_SOURCE = "configurationProperties";
    private static final String SECRETS_PROPERTIES_SOURCE = "secretProperties";
    private static final String NAME_VALUE_DELIMITER = "=";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        Map<String, Object> properties = new LinkedHashMap<>(0);
        environment.getSystemEnvironment().entrySet().stream()
                .filter(entry -> entry.getKey().endsWith(SECRET_FILE_SUFFIX))
                .forEach(entry -> properties.putAll(getProperty(Path.of((String) entry.getValue()))));
        environment.getPropertySources().addBefore(SPRING_BOOT_PROPERTIES_SOURCE,
                new MapPropertySource(SECRETS_PROPERTIES_SOURCE, properties));
    }

    private Map<String, String> getProperty(Path path) {
        if (!Files.exists(path)) {
            throw new RuntimeException("Cannot read file because it is not found in path " + path);
        }
        String content;
        try {
            content = Files.readString(path, StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file in path " + path, e);
        }
        if (!content.matches(".*" + NAME_VALUE_DELIMITER + ".*")) {
            throw new RuntimeException("Unsupported secret format, must be: property=secret");
        }
        String[] property = content.split(NAME_VALUE_DELIMITER);
        return Map.of(property[0], property[1]);
    }
}
