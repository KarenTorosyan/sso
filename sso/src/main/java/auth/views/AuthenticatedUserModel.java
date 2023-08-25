package auth.views;

import auth.configs.security.TypedUserDetails;
import auth.entities.user.User;
import auth.entities.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserModel {

    public static final String PRINCIPAL_NAME_ATTR = "principalName";
    public static final String USER_ATTR = "user";

    private final UserService userService;

    public void mutate(Model model, AuthenticatedPrincipal principal) {
        if (principal instanceof TypedUserDetails typedUserDetails) {
            model.addAttribute(PRINCIPAL_NAME_ATTR, typedUserDetails.getUsername());
            model.addAttribute(USER_ATTR, typedUserDetails.getUser());
        } else {
            User user = userService.getByEmail(principal.getName());
            model.addAttribute(PRINCIPAL_NAME_ATTR, user.getEmail().getAddress());
            model.addAttribute(USER_ATTR, user);
        }
    }
}
