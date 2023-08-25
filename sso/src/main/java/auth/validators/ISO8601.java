package auth.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeISO8601Validator.class)
public @interface ISO8601 {

    String DEFAULT_MESSAGE = "The datetime must be ISO8601 standard!";

    String message() default DEFAULT_MESSAGE;

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
