package auth.entities.user;

import auth.Endpoints;
import auth.configs.Resources;
import auth.errors.Errors;
import auth.intergrations.smtp.SmtpService;
import jakarta.mail.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class EmailVerifierImpl implements EmailVerifier {

    private final SmtpService smtpService;

    private final Resources resources;

    @Override
    public void verify(String baseUrl, Email email) {
        String verificationLink = ServletUriComponentsBuilder.fromUriString(baseUrl)
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
        smtpService.sendMimeMessage(content, "text/html",
                "Email verification", Message.RecipientType.TO, email.getAddress());
    }
}
