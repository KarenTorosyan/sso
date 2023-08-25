package auth.entities.user;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Objects;

@Getter
@ToString
public class Account {

    private Instant lockedIn;

    private Instant disabledIn;

    private Instant expiresIn;

    private Instant credentialExpiresIn;

    public Account lockedIn(Instant lockedIn) {
        this.lockedIn = lockedIn;
        return this;
    }

    public boolean isLocked() {
        return lockedIn != null && lockedIn.isBefore(Instant.now());
    }

    public Account disabledIn(Instant disabledIn) {
        this.disabledIn = disabledIn;
        return this;
    }

    public boolean isDisabled() {
        return disabledIn != null && disabledIn.isBefore(Instant.now());
    }

    public Account expiresIn(Instant expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public boolean isExpired() {
        return expiresIn != null && expiresIn.isBefore(Instant.now());
    }

    public Account credentialsExpiresIn(Instant credentialExpiresIn) {
        this.credentialExpiresIn = credentialExpiresIn;
        return this;
    }

    public boolean isCredentialsExpired() {
        return credentialExpiresIn != null && credentialExpiresIn.isBefore(Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(getLockedIn(), account.getLockedIn()) &&
                Objects.equals(getDisabledIn(), account.getDisabledIn()) &&
                Objects.equals(getExpiresIn(), account.getExpiresIn()) &&
                Objects.equals(getCredentialExpiresIn(), account.getCredentialExpiresIn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLockedIn(), getDisabledIn(),
                getExpiresIn(), getCredentialExpiresIn());
    }
}
