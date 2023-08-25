package auth.mock;

import auth.entities.authority.Authority;

public class MockAuthority implements Mock<Authority> {

    @Override
    public Authority mock() {
        return new Authority("SCOPE_admin")
                .withId("1")
                .withDescription("Scope for admins");
    }
}
