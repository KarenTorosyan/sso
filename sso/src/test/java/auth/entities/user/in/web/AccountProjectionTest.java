package auth.entities.user.in.web;

import auth.entities.user.Account;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountProjectionTest {

    @Test
    void shouldBuildResponse() {
        Account account = new Account();
        AccountProjection accountProjection = AccountProjection.from(account);
        assertThat(accountProjection)
                .matches(a -> a.lockedIn() == account.getLockedIn() &&
                        a.disabledIn() == account.getDisabledIn() &&
                        a.expiresIn() == account.getDisabledIn() &&
                        a.credentialsExpiresIn() == account.getCredentialExpiresIn());
    }
}
