package auth.views;

import auth.Endpoints;
import auth.entities.user.Password;
import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.Errors;
import auth.errors.NotExistsException;
import auth.utils.MessageSourceUtils;
import auth.validators.LimitPasswordSize;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
@SessionAttributes("user")
public class PasswordRestoreController {

    private final UserService userService;

    private final MessageSource messageSource;

    private final PasswordEncoder passwordEncoder;

    @GetMapping(Endpoints.PASSWORD_RESTORE)
    String renderPasswordRestorePage(@RequestParam String email,
                                     @RequestParam String code,
                                     Model model) {

        // when user by email not found
        User user;
        try {
            user = userService.getByEmail(email);
        } catch (NotExistsException e) {
            model.addAttribute("userNotFoundByEmail",
                    MessageSourceUtils.localize(messageSource, e));
            return "password-forget-restore";
        }

        model.addAttribute("user", user);

        // when restore code invalid
        if (user.getPassword().getRestoreCode() == null ||
                !user.getPassword().getRestoreCode().equals(code)) {
            model.addAttribute("passwordRestoreCodeInvalid",
                    MessageSourceUtils.localize(messageSource,
                            Errors.passwordRestoreCodeInvalid(email, code)));
            return "password-forget-restore";
        }

        // when restore code expired
        Instant exp = user.getPassword().getRestoreCodeExpiresIn();
        if (exp != null && Instant.now().isAfter(exp)) {
            model.addAttribute("passwordRestoreCodeExpired",
                    MessageSourceUtils.localize(messageSource,
                            Errors.passwordRestoreCodeExpired(exp)));
            return "password-forget-restore";
        }

        return "password-forget-restore";
    }

    @PostMapping(Endpoints.PASSWORD_RESTORE)
    String passwordRestoreRequest(@RequestParam String newPassword,
                                  @RequestParam String confirmNewPassword,
                                  @ModelAttribute("user") User user,
                                  Model model) {

        // when password not confirmed
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("passwordNotConfirmed",
                    MessageSourceUtils.localize(messageSource,
                            Errors.passwordNotConfirmed()));
            return "password-forget-restore";
        }

        // when password length validation errors
        int min = LimitPasswordSize.PASSWORD_MIN_LENGTH;
        int max = LimitPasswordSize.PASSWORD_MAX_LENGTH;
        if (newPassword.length() < min || newPassword.length() > max) {
            model.addAttribute("passwordLengthValidation",
                    MessageSourceUtils.localize(messageSource,
                            Errors.passwordLengthValidation(min, max)));
            return "password-forget-restore";
        }

        // restore password
        user.withPassword(new Password(passwordEncoder.encode(newPassword))
                .withRestoreCode(null)
                .withRestoreCodeExpiresIn(null));
        userService.edit(user);

        model.addAttribute("successPasswordRestore", true);
        return "login";
    }
}
