package auth.intergrations.filesystem;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String upload(MultipartFile multipartFile);

    void validateImageExtension(String name);
}
