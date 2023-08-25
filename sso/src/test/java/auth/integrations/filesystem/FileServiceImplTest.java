package auth.integrations.filesystem;

import auth.configs.files.FilesProperties;
import auth.configs.files.Images;
import auth.intergrations.filesystem.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ContextConfiguration(classes = FileServiceImpl.class)
public class FileServiceImplTest {

    @Autowired
    private FileServiceImpl fileService;

    @MockBean
    private MultipartProperties multipartProperties;

    @MockBean

    private FilesProperties filesProperties;

    @Test
    void shouldUploadFileAndReturnUrl(@TempDir Path location) {
        String originalFilename = "file.ext";
        String filename = "file";
        String filePrefix = "prefix_";
        String contentType = "type/ext";
        given(multipartProperties.getLocation()).willReturn(location.toString());
        given(filesProperties.getImages()).willReturn(new Images());
        MockMultipartFile multipartFile = new MockMultipartFile(filename, originalFilename, contentType, new byte[]{0});
        fileService.setFilePrefix(filePrefix);
        String fileNameWithPrefix = fileService.upload(multipartFile);
        assertThat(fileNameWithPrefix).isEqualTo(filePrefix + originalFilename);
    }
}
