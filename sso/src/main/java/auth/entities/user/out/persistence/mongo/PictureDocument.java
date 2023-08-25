package auth.entities.user.out.persistence.mongo;

import auth.entities.picture.Picture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PictureDocument {

    private String url;

    public static PictureDocument from(Picture picture) {
        return picture == null ? null : new PictureDocument(picture.getUrl());
    }

    public Picture getPicture() {
        return url == null ? null : new Picture(url);
    }
}
