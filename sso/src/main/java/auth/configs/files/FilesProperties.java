package auth.configs.files;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@ConfigurationProperties("files")
public class FilesProperties {

    @NestedConfigurationProperty
    private final Images images = new Images();
}
