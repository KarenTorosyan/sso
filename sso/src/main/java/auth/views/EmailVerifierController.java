package auth.views;

import auth.Endpoints;
import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.Errors;
import auth.utils.MessageSourceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

@Controller
@RequestMapping(Endpoints.EMAIL_VERIFY)
@RequiredArgsConstructor
public class EmailVerifierController {

    private final UserService userService;

    private final MessageSource messageSource;

    @GetMapping
    String verifyEmail(@RequestParam String email, @RequestParam String code, Model model) {
        User user = userService.getByEmail(email);

        // when email verification code invalid
        String verificationCode = user.getEmail().getVerificationCode();
        if (verificationCode == null || !verificationCode.equals(code)) {
            model.addAttribute("emailVerificationCodeInvalid",
                    MessageSourceUtils.localize(messageSource,
                            Errors.emailVerificationCodeInvalid(user.getEmail().getVerificationCode())));
            return "login";
        }

        // when email verification code expired
        Instant verificationCodeExpiresIn = user.getEmail().getVerificationCodeExpiresIn();
        if (verificationCodeExpiresIn != null && Instant.now().isAfter(verificationCodeExpiresIn)) {
            model.addAttribute("emailVerificationCodeExpired",
                    MessageSourceUtils.localize(messageSource,
                            Errors.emailVerificationCodeExpired(verificationCodeExpiresIn)));
            return "login";
        }

        userService.edit(user.withEmail(user.getEmail()
                .verified(true)
                .withVerificationCode(null)
                .withVerificationCodeExpiresIn(null)));

        model.addAttribute("emailVerified", true);

        return "login";
    }
}
