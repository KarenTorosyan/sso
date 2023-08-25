package auth.views;

import auth.Endpoints;
import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.Errors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(Endpoints.EMAIL_VERIFY)
@RequiredArgsConstructor
public class EmailVerifierController {

    private final UserService userService;

    private final LocalizedNotifications localizedNotifications;

    @GetMapping("/{email}")
    String verifyEmail(@PathVariable String email, @RequestParam String code, Model model) {
        User user = userService.getByEmail(email);
        if (!user.getEmail().getVerificationCode().equals(code)) {
            throw Errors.emailVerificationCodeInvalid(user.getEmail().getVerificationCode());
        }
        userService.edit(user.withEmail(user.getEmail().verified(true)));
        model.addAttribute("successEmailVerifiedText", localizedNotifications.yourEmailVerified());
        return "login";
    }
}
