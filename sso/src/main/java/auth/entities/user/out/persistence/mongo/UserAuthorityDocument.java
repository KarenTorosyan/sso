package auth.entities.user.out.persistence.mongo;

import auth.entities.authority.out.persistence.mongo.AuthorityDocument;
import auth.entities.user.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("userAuthorities")
public class UserAuthorityDocument {

    @Id
    private String id;

    @Field("email")
    private String userId;

    @Field("authority")
    @DBRef
    private AuthorityDocument authorityDocument;

    public UserAuthorityDocument(String userId, AuthorityDocument authorityDocument) {
        this.id = new ObjectId().toHexString();
        this.userId = userId;
        this.authorityDocument = authorityDocument;
    }

    public static UserAuthorityDocument from(UserAuthority userAuthority) {
        return new UserAuthorityDocument(
                userAuthority.getId() != null ? userAuthority.getId() : new ObjectId().toHexString(),
                userAuthority.getUserId(),
                AuthorityDocument.from(userAuthority.getAuthority())
        );
    }

    public UserAuthority getUserAuthority() {
        return new UserAuthority(id, userId, authorityDocument.getAuthority());
    }
}
