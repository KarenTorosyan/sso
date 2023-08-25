package auth.entities.user.in.web;

import auth.entities.picture.in.web.PictureProjection;
import auth.entities.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserProjection(@JsonProperty("id") String id,
                             @JsonProperty("name") String name,
                             @JsonProperty("familyName") String familyName,
                             @JsonProperty("email") EmailProjection emailPrimaryProjection,
                             @JsonProperty("picture") PictureProjection pictureProjection) {

    public static UserProjection from(User user) {
        return new UserProjection(
                user.getId(),
                user.getName(),
                user.getFamilyName(),
                EmailProjection.from(user.getEmail()),
                PictureProjection.from(user.getPicture()));
    }
}
