package auth.entities.authority.out.persistence.mongo;

import auth.entities.authority.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("authority")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    @TextIndexed(weight = 5.0f)
    @Field("name")
    private String name;

    @TextIndexed
    @Field("description")
    private String description;

    public static AuthorityDocument from(Authority authority) {
        return new AuthorityDocument(
                authority.getId() != null ? authority.getId() : new ObjectId().toHexString(),
                authority.getName(),
                authority.getDescription()
        );
    }

    public Authority getAuthority() {
        return new Authority(name)
                .withId(id)
                .withDescription(description);
    }
}
