package auth.entities.user.out.persistence.mongo;

import auth.entities.user.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDocument {

    @Field("locked")
    private Instant lockedIn;

    @Field("disabled")
    private Instant disabledIn;

    @Field("expiredIn")
    private Instant expiresIn;

    @Field("credentialsExpiresIn")
    private Instant credentialsExpiresIn;

    public static AccountDocument from(Account account) {
        return new AccountDocument(
                account.getLockedIn(),
                account.getDisabledIn(),
                account.getExpiresIn(),
                account.getCredentialExpiresIn()
        );
    }

    public Account getAccount() {
        return new Account()
                .lockedIn(lockedIn)
                .disabledIn(disabledIn)
                .expiresIn(expiresIn)
                .credentialsExpiresIn(credentialsExpiresIn);
    }
}
