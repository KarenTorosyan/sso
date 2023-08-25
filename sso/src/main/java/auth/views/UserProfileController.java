package auth.views;

import auth.Endpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserProfileController {

    private final AuthenticatedUserModel authenticatedUserModel;

    @GetMapping(Endpoints.PROFILE)
    String profile(@AuthenticationPrincipal AuthenticatedPrincipal principal, Model model) {
        authenticatedUserModel.mutate(model, principal);
        return "profile";
    }
}

