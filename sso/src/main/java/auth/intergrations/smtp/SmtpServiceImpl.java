package auth.intergrations.smtp;

import auth.errors.Errors;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmtpServiceImpl implements SmtpService {

    private final JavaMailSender javaMailSender;

    private final MailProperties mailProperties;

    @Override
    public void sendMimeMessage(String content,
                                String contentType,
                                String subject,
                                Message.RecipientType recipientType,
                                String addresses) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        if (mailProperties.getUsername() == null) {
            throw Errors.mailSenderRequired();
        }
        try {
            mimeMessage.setFrom(mailProperties.getUsername());
            mimeMessage.setContent(content, contentType);
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipients(recipientType, addresses);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }
}
