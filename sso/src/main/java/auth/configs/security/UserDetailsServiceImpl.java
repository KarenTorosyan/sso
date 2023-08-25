package auth.configs.security;

import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.LocalizedErrors;
import auth.errors.NotExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getUser(username);

        LocalizedErrors localizedErrors = getLocalizedErrors();

        if (user.getAccount().isLocked()) {
            throw localizedErrors.accountLocked(username, user.getAccount().getLockedIn());
        }
        if (user.getAccount().isDisabled()) {
            throw localizedErrors.userDisabled(username, user.getAccount().getDisabledIn());
        }
        if (user.getAccount().isCredentialsExpired()) {
            throw localizedErrors.credentialsExpired(username, user.getAccount().getCredentialExpiresIn());
        }
        if (user.getAccount().isExpired()) {
            throw localizedErrors.accountExpired(username, user.getAccount().getExpiresIn());
        }
        Set<GrantedAuthority> authorities = userService.getAuthorities(user.getId(), Pageable.unpaged())
                .getContent().stream()
                .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.getAuthority().getName()))
                .collect(Collectors.toSet());

        return new TypedUserDetails(user, authorities);
    }

    private LocalizedErrors getLocalizedErrors() {
        return new LocalizedErrors(messageSource);
    }

    private User getUser(String username) {
        try {
            return userService.getByEmail(username);
        } catch (NotExistsException e) {
            throw getLocalizedErrors().userNotExists(username);
        }
    }
}
