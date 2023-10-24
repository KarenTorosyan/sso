package auth.configs.security;

import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.NotExistsException;
import lombok.RequiredArgsConstructor;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getUser(username);

        Set<GrantedAuthority> authorities = userService.getAuthorities(user.getId(), Pageable.unpaged())
                .getContent().stream()
                .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.getAuthority().getName()))
                .collect(Collectors.toSet());

        return new TypedUserDetails(user, authorities);
    }

    private User getUser(String username) {
        try {
            return userService.getByEmail(username);
        } catch (NotExistsException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
