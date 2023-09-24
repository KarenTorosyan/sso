package auth.entities.user;

import auth.Endpoints;
import auth.errors.Errors;
import auth.intergrations.smtp.SmtpService;
import jakarta.mail.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = EmailVerifierImpl.class)
public class EmailVerifierImplTest {

    @Autowired
    private EmailVerifierImpl emailVerifier;

    @MockBean
    private SmtpService smtpService;

    @Test
    void shouldSendEmailVerificationMessage() {
        Email email = new Email("karen.torosyan@outlook.com");
        emailVerifier.verify("http://localhost", email);

        String verificationUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath(Endpoints.EMAIL_VERIFY.concat("/{email}"))
                .replaceQueryParam("code", email.getVerificationCode())
                .build(email.getAddress()).toString();
        String template = "templates/email-verification.html";
        String content;
        try {
            content = Files.readString(new ClassPathResource(template).getFile().toPath())
                    .replaceAll("#verificationUri", verificationUri);
        } catch (IOException e) {
            throw Errors.fileNotFound(template);
        }
        verify(smtpService).sendMimeMessage(content, "text/html",
                "VerificationSubject", Message.RecipientType.TO, email.getAddress());
    }
}
