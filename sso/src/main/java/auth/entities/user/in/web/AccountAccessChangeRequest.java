package auth.entities.user.in.web;

import auth.entities.user.Account;
import auth.validators.ISO8601;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

public record AccountAccessChangeRequest(
        @ISO8601 String lockedIn,
        @ISO8601 String disabledIn,
        @ISO8601 String expiresIn,
        @ISO8601 String credentialsExpiresIn) {

    @JsonIgnore
    public Account getAccount() {
        return new Account()
                .lockedIn(Instant.parse(lockedIn))
                .disabledIn(Instant.parse(disabledIn))
                .expiresIn(Instant.parse(expiresIn))
                .credentialsExpiresIn(Instant.parse(credentialsExpiresIn));
    }
}
