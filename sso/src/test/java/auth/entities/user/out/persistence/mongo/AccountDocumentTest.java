package auth.entities.user.out.persistence.mongo;

import auth.entities.user.Account;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountDocumentTest {

    @Test
    void shouldCreateMongoDocumentInstanceFromAccount() {
        Account account = new Account();
        AccountDocument accountDocument = AccountDocument.from(account);
        assertThat(accountDocument)
                .matches(a -> a.getLockedIn() == account.getLockedIn() &&
                        a.getDisabledIn() == account.getDisabledIn() &&
                        a.getExpiresIn() == account.getExpiresIn() &&
                        a.getCredentialsExpiresIn() == account.getExpiresIn() &&
                        a.getAccount().equals(account));
    }
}
