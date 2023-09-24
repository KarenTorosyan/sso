package auth.configs.verification;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("verification")
public class VerificationProperties {

    private boolean sendEmail;

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }
}
