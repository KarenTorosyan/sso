package auth;

import auth.configs.Resources;
import auth.configs.web.WebConfig;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({WebConfig.class, MultipartProperties.class, Resources.class})
public @interface IntegrateWebConfig {
}
