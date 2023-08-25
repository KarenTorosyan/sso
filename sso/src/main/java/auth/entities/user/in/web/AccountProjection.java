package auth.entities.user.in.web;

import auth.entities.user.Account;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AccountProjection(@JsonProperty("lockedIn") Instant lockedIn,
                                @JsonProperty("disabledIn") Instant disabledIn,
                                @JsonProperty("expiresIn") Instant expiresIn,
                                @JsonProperty("credentialsExpiresIn") Instant credentialsExpiresIn) {

    public static AccountProjection from(Account account) {
        return new AccountProjection(
                account.getLockedIn(),
                account.getDisabledIn(),
                account.getExpiresIn(),
                account.getCredentialExpiresIn()
        );
    }
}
