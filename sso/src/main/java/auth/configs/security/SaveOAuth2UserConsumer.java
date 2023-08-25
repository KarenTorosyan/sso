package auth.configs.security;

import auth.entities.user.Email;
import auth.entities.user.User;
import auth.entities.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SaveOAuth2UserConsumer implements Consumer<OAuth2User> {

    private final UserService userService;

    @Override
    public void accept(OAuth2User oAuth2User) {
        if (!userService.existsByEmail(oAuth2User.getName())) {
            userService.create(new User()
                    .withEmail(new Email(oAuth2User.getName())
                            .verified(true)));
        }
    }
}
