package auth.configs.files;

import lombok.Getter;

import java.util.Set;

@Getter
public class Images {

    private static final Set<String> DEFAULT_SUPPORTED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif");

    private Set<String> supportedExtensions = DEFAULT_SUPPORTED_EXTENSIONS;

    public void setSupportedExtensions(Set<String> supportedExtensions) {
        this.supportedExtensions = supportedExtensions;
    }
}
