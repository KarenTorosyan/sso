package auth.entities.picture.in.web;

import auth.entities.picture.Picture;
import org.junit.jupiter.api.Test;
import auth.mock.MockPicture;

import static org.assertj.core.api.Assertions.assertThat;

public class PictureProjectionTest {

    @Test
    void shouldBuildResponse() {
        Picture picture = MockPicture.mock();
        PictureProjection pictureProjection = PictureProjection.from(picture);
        assertThat(pictureProjection)
                .matches(projection -> projection.url().equals(picture.getUrl()));
    }
}
