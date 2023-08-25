package auth.entities.picture;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PictureTest {

    @Test
    void shouldCreateInstance() {
        String url = "url";
        Picture picture = new Picture(url);
        assertThat(picture)
                .matches(p -> p.getUrl().equals(url));
    }
}
