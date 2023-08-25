package auth.entities.authority.in.web;

import auth.entities.authority.Authority;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorityProjection(@JsonProperty("id") Object id,
                                  @JsonProperty("name") String name,
                                  @JsonProperty("description") String description) {

    public static AuthorityProjection from(Authority authority) {
        return new AuthorityProjection(
                authority.getId(),
                authority.getName(),
                authority.getDescription()
        );
    }
}
