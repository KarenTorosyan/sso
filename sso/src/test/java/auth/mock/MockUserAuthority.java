package auth.mock;

import auth.entities.user.UserAuthority;

public class MockUserAuthority implements Mock<UserAuthority> {

    private final MockUser mockUser = new MockUser();

    private final MockAuthority mockAuthority = new MockAuthority();

    @Override
    public UserAuthority mock() {
        return new UserAuthority(mockUser.mock().getId(), mockAuthority.mock())
                .withId("1");
    }
}
