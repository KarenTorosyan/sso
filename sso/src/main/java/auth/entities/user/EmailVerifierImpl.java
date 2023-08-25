package auth.entities.user;

import auth.Endpoints;
import auth.errors.Errors;
import auth.intergrations.smtp.SmtpService;
import jakarta.mail.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class EmailVerifierImpl implements EmailVerifier {

    private final SmtpService smtpService;

    @Override
    public void verify(Email email) {
        String verificationUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath(Endpoints.EMAIL_VERIFY.concat("/{email}"))
                .replaceQueryParam("code", email.getVerificationCode())
                .build(email.getAddress()).toString();
        String template = "templates/email-verification.html";
        String content;
        try {
            InputStream inputStream = new ClassPathResource(template).getInputStream();
            content = new String(inputStream.readAllBytes())
                    .replaceAll("#verificationUri", verificationUri);
        } catch (IOException e) {
            throw Errors.fileNotFound(template);
        }
        smtpService.sendMimeMessage(content, "text/html",
                "VerificationSubject", Message.RecipientType.TO, email.getAddress());
    }
}
