package auth.intergrations.smtp;

import jakarta.mail.Message;

public interface SmtpService {

    void sendMimeMessage(String content,
                         String contentType,
                         String subject,
                         Message.RecipientType recipientType,
                         String addresses);
}
