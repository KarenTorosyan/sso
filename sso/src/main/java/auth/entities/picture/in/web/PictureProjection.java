package auth.entities.picture.in.web;

import auth.entities.picture.Picture;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PictureProjection(@JsonProperty("url") String url) {

    public static PictureProjection from(Picture picture) {
        return picture == null ? null : new PictureProjection(picture.getUrl());
    }
}
