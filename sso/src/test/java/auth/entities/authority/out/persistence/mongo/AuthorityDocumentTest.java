package auth.entities.authority.out.persistence.mongo;

import auth.entities.authority.Authority;
import org.junit.jupiter.api.Test;
import auth.mock.MockAuthority;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityDocumentTest {

    @Test
    void shouldCreateMongoDocumentInstanceFromAuthority() {
        Authority authority = new MockAuthority().mock();
        assertThat(AuthorityDocument.from(authority))
                .matches(a -> a.getId().equals(authority.getId()) &&
                        a.getName().equals(authority.getName()) &&
                        a.getDescription().equals(authority.getDescription()) &&
                        a.getAuthority().equals(authority));
    }
}
