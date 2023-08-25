package auth.entities.user;

import auth.entities.authority.Authority;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserAuthority {

    private String id;

    private String userId;

    private Authority authority;

    public UserAuthority(String userId, Authority authority) {
        this.userId = userId;
        this.authority = authority;
    }

    public record Id(String value) {
    }

    public UserAuthority withId(String id) {
        this.id = id;
        return this;
    }

    public UserAuthority withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserAuthority withAuthority(Authority authority) {
        this.authority = authority;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthority that = (UserAuthority) o;
        return Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getAuthority(), that.getAuthority());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getAuthority());
    }
}
