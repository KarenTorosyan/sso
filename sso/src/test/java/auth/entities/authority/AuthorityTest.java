package auth.entities.authority;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityTest {

    @Test
    void shouldCreateInstance() {
        String name = "SCOPE_user";
        Authority authority = new Authority(name);
        assertThat(authority).matches(a -> a.getId() == null &&
                a.getName().equals(name) &&
                a.getDescription() == null
        );

        String id = "1";
        String description = "Scope for users";
        authority.withId(id)
                .withDescription(description);
        assertThat(authority)
                .matches(a -> a.getId().equals(id) &&
                        a.getName().equals(name) &&
                        a.getDescription().equals(description));
    }
}
