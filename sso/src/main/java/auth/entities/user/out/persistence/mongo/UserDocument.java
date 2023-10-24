package auth.entities.user.out.persistence.mongo;

import auth.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("user")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument {

    @Id
    private String id;

    @TextIndexed
    @Field("name")
    private String name;

    @TextIndexed
    @Field("familyName")
    private String familyName;

    @Field("email")
    private EmailDocument emailDocument;

    @Field("password")
    private PasswordDocument passwordDocument;

    @Field("picture")
    private PictureDocument pictureDocument;

    @Field("account")
    private AccountDocument accountDocument;

    public static UserDocument from(User user) {
        return new UserDocument(
                user.getId() != null ? user.getId() : new ObjectId().toHexString(),
                user.getName(),
                user.getFamilyName(),
                EmailDocument.from(user.getEmail()),
                PasswordDocument.from(user.getPassword()),
                PictureDocument.from(user.getPicture()),
                AccountDocument.from(user.getAccount())
        );
    }

    public User getUser() {
        return new User(emailDocument.getEmail(), passwordDocument.getPassword())
                .withId(id)
                .withName(name)
                .withFamilyName(familyName)
                .withPicture(pictureDocument != null ? pictureDocument.getPicture() : null)
                .withAccount(accountDocument.getAccount());
    }
}
