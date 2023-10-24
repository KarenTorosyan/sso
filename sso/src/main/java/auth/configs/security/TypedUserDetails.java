package auth.configs.security;

import auth.entities.user.User;
import lombok.Getter;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Getter
public class TypedUserDetails implements UserDetails, AuthenticatedPrincipal {

    private final User user;

    private final Set<? extends GrantedAuthority> authorities;

    public TypedUserDetails(User user, Set<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword().getValue();
    }

    @Override
    public String getUsername() {
        return user.getEmail().getAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.getAccount().isExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getAccount().isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.getAccount().isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return !user.getAccount().isDisabled();
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypedUserDetails that = (TypedUserDetails) o;
        return Objects.equals(getUser(), that.getUser()) && Objects.equals(getAuthorities(), that.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getAuthorities());
    }
}
