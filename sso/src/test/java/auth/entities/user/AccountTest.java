package auth.entities.user;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {

    @Test
    void shouldCreateInstance() {
        Account account = new Account();
        assertThat(account).matches(a -> a.getLockedIn() == null &&
                a.getDisabledIn() == null &&
                a.getExpiresIn() == null &&
                a.getCredentialExpiresIn() == null);

        Instant lockedIn = Instant.MIN;
        Instant disabledIN = Instant.MIN;
        Instant expiresIn = Instant.MIN;
        Instant credentialsExpiresIn = Instant.MIN;

        account.lockedIn(lockedIn)
                .disabledIn(disabledIN)
                .expiresIn(expiresIn)
                .credentialsExpiresIn(credentialsExpiresIn);
        assertThat(account).matches(a -> a.getLockedIn() == lockedIn &&
                a.getDisabledIn() == disabledIN &&
                a.getExpiresIn() == expiresIn &&
                a.getCredentialExpiresIn() == credentialsExpiresIn);
    }
}
