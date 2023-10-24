package auth.configs;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties("resources")
@Getter
public class Resources {

    private Resource verificationEmailMessageTemplate;

    private Resource passwordForgetEmailMessageTemplate;

    public void setVerificationEmailMessageTemplate(Resource verificationEmailMessageTemplate) {
        this.verificationEmailMessageTemplate = verificationEmailMessageTemplate;
    }

    public void setPasswordForgetEmailMessageTemplate(Resource passwordForgetEmailMessageTemplate) {
        this.passwordForgetEmailMessageTemplate = passwordForgetEmailMessageTemplate;
    }
}
