package auth.integrations.smtp;

import auth.intergrations.smtp.SmtpServiceImpl;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@ContextConfiguration(classes = SmtpServiceImpl.class)
public class SmtpServiceImplTest {

    @Autowired
    private SmtpServiceImpl smtpService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private MailProperties mailProperties;

    @Test
    void shouldSendMimeMessage() {
        String from = "address";
        String content = "content";
        String contentType = "text/plain";
        String subject = "subject";
        String recipients = "recipient1, recipient2";
        given(mailProperties.getUsername()).willReturn(from);
        given(javaMailSender.createMimeMessage()).willReturn(new JavaMailSenderImpl().createMimeMessage());
        Message.RecipientType recipientType = MimeMessage.RecipientType.TO;
        smtpService.sendMimeMessage(content, contentType, subject, recipientType, recipients);
    }
}
