package auth.entities.user.out.persistence.mongo;

import auth.entities.picture.Picture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PictureDocumentTest {

    @Test
    void shouldCreateMongoDocumentFromPicture() {
        Picture picture = new Picture("url");
        PictureDocument pictureDocument = PictureDocument.from(picture);
        assertThat(pictureDocument)
                .matches(p -> p.getUrl().equals(picture.getUrl()) &&
                        p.getPicture().equals(picture));
    }
}
