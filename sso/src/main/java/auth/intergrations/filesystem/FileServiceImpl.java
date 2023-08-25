package auth.intergrations.filesystem;

import auth.configs.files.FilesProperties;
import auth.errors.Errors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MultipartProperties multipartProperties;

    @Getter
    private final FilesProperties filesProperties;

    @Setter
    private String filePrefix = System.currentTimeMillis() + "_";

    @Override
    public String upload(MultipartFile multipartFile) {
        createDirectoryIfNotExists(Path.of(multipartProperties.getLocation()));
        String fileNameWithPrefix = filePrefix + multipartFile.getOriginalFilename();
        String destination = addSlashToEndIfNotExists(multipartProperties.getLocation()) + fileNameWithPrefix;
        try {
            multipartFile.transferTo(Path.of(destination));
            return fileNameWithPrefix;
        } catch (IOException e) {
            throw Errors.fileTransfer(e, destination);
        }
    }

    private void createDirectoryIfNotExists(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw Errors.directoryCreation(e, path.toUri().getPath());
            }
        }
    }

    private String addSlashToEndIfNotExists(String directory) {
        return directory.endsWith("/") ? directory : directory + "/";
    }

    @Override
    public void validateImageExtension(String name) {
        Set<String> supportedExtensions = filesProperties.getImages().getSupportedExtensions();
        supportedExtensions.stream()
                .filter(name::endsWith)
                .findAny().orElseThrow(() -> Errors.fileExtensionNotSupported(supportedExtensions));
    }
}
