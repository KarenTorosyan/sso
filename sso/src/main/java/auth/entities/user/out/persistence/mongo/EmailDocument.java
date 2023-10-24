package auth.entities.user.out.persistence.mongo;

import auth.entities.user.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailDocument {

    @Indexed(unique = true)
    @TextIndexed
    @Field("address")
    private String address;

    @Field("verificationCode")
    private String verificationCode;

    private Instant verificationCodeExpiresIn;

    @Field("verified")
    private boolean verified;

    public static EmailDocument from(Email email) {
        return new EmailDocument(
                email.getAddress(),
                email.getVerificationCode(),
                email.getVerificationCodeExpiresIn(),
                email.isVerified()
        );
    }

    public Email getEmail() {
        return new Email(address, verificationCode,
                verificationCodeExpiresIn, verified);
    }
}
