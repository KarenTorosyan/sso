package auth.validators;

import jakarta.validation.constraints.Size;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Size(message = "{password_minmax}",
        min = LimitPasswordSize.PASSWORD_MIN_LENGTH,
        max = LimitPasswordSize.PASSWORD_MAX_LENGTH
)
public @interface LimitPasswordSize {
    int PASSWORD_MIN_LENGTH = 6;
    int PASSWORD_MAX_LENGTH = 20;
}
