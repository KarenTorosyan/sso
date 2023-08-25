package auth.configs.security;

import auth.entities.picture.Picture;
import auth.entities.user.Email;
import auth.entities.user.User;
import auth.entities.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SaveOidcUserConsumer implements Consumer<OidcUser> {

    private final UserService userService;

    @Override
    public void accept(OidcUser oidcUser) {
        if (!userService.existsByEmail(oidcUser.getEmail())) {
            userService.create(new User()
                    .withEmail(new Email(oidcUser.getEmail())
                            .verified(oidcUser.getEmailVerified() != null && oidcUser.getEmailVerified()))
                    .withName(oidcUser.getGivenName())
                    .withFamilyName(oidcUser.getFamilyName())
                    .withPicture(new Picture(oidcUser.getPicture())));
        }
    }
}
