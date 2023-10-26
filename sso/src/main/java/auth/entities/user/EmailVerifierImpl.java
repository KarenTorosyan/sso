package auth.entities.user;

import auth.Endpoints;
import auth.configs.Resources;
import auth.errors.Errors;
import auth.intergrations.smtp.SmtpService;
import jakarta.mail.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class EmailVerifierImpl implements EmailVerifier {

    private final SmtpService smtpService;

    private final Resources resources;

    private boolean asynchronously = true;

    @Override
    public void verify(HttpServletRequest request, Email email) {
        String verificationLink = ServletUriComponentsBuilder.fromRequest(request)
                .replacePath(Endpoints.EMAIL_VERIFY)
                .replaceQueryParam("email", email.getAddress())
                .replaceQueryParam("code", email.getVerificationCode())
                .build(email.getAddress()).toString();
        Resource verificationEmailMessage = resources.getVerificationEmailMessageTemplate();
        String content;
        try {
            InputStream inputStream = verificationEmailMessage.getInputStream();
            content = new String(inputStream.readAllBytes())
                    .replaceAll("#link", verificationLink);
        } catch (IOException e) {
            throw Errors.fileNotFound(verificationEmailMessage.getFilename());
        }
        Runnable sendEmail = () -> smtpService.sendMimeMessage(content, "text/html",
                "Email verification", Message.RecipientType.TO, email.getAddress());
        if (asynchronously) {
            CompletableFuture.runAsync(sendEmail, Executors.newSingleThreadExecutor())
                    .exceptionally(throwable -> {
                        throwable.printStackTrace(System.err);
                        return null;
                    });
        } else sendEmail.run();
    }
}
