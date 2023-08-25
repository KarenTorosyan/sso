package auth.entities.user.in.web;

import auth.entities.user.Email;
import com.fasterxml.jackson.annotation.JsonProperty;

public record EmailProjection(@JsonProperty("address") String address,
                              @JsonProperty("verified") boolean verified) {

    public static EmailProjection from(Email email) {
        return new EmailProjection(email.getAddress(), email.isVerified());
    }
}
