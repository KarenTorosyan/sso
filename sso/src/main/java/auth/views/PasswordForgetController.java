package auth.views;

import auth.Endpoints;
import auth.configs.Resources;
import auth.entities.user.User;
import auth.entities.user.UserService;
import auth.errors.Errors;
import auth.errors.InvalidationException;
import auth.errors.NotExistsException;
import auth.intergrations.smtp.SmtpService;
import auth.utils.MessageSourceUtils;
import jakarta.mail.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Controller
@RequiredArgsConstructor
public class PasswordForgetController {

    private final UserService userService;

    private final MessageSource messageSource;

    private final SmtpService smtpService;

    private final Resources resources;

    @GetMapping(Endpoints.PASSWORD_FORGET)
    String renderPasswordForgetPage() {
        return "password-forget";
    }

    @PostMapping(Endpoints.PASSWORD_FORGET)
    String passwordRestoreRequest(@RequestParam String email, Model model) {

        // when email is empty
        if (email == null || email.isBlank()) {
            InvalidationException invalidationError = Errors.emailAddressInvalid();
            model.addAttribute("emailInvalid",
                    MessageSourceUtils.localize(messageSource, invalidationError));
            return "password-forget";
        }

        // when user by email not found
        User user;
        try {
            user = userService.getByEmail(email);
        } catch (NotExistsException e) {
            model.addAttribute("userNotFoundByEmail",
                    MessageSourceUtils.localize(messageSource, e));
            return "password-forget";
        }

        // set password restore code to user
        String restoreCode = UUID.randomUUID().toString();
        userService.edit(user.withPassword(user.getPassword()
                .withRestoreCode(restoreCode)
                .withRestoreCodeExpiresIn(Instant.now()
                        .plus(12, ChronoUnit.HOURS))));

        // build message
        String content;
        String linkToRestorePassword = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath(Endpoints.PASSWORD_RESTORE)
                .queryParam("email", email)
                .queryParam("code", restoreCode)
                .toUriString();
        try {
            // error on read template
            content = new String(resources.getPasswordForgetEmailMessageTemplate()
                    .getInputStream().readAllBytes())
                    .replaceAll("#link", linkToRestorePassword);
        } catch (IOException e) {
            model.addAttribute("somethingWentWrong", true);
            return "password-forget";
        }

        // sent email
        Runnable sendEmail = () ->
                smtpService.sendMimeMessage(content, "text/html",
                        "Restore access", Message.RecipientType.TO, email);
        CompletableFuture.runAsync(sendEmail, Executors.newSingleThreadExecutor())
                .exceptionally(throwable -> {
                    throwable.printStackTrace(System.err);
                    return null;
                });
        model.addAttribute("emailSent", true);

        return "password-forget";
    }
}
