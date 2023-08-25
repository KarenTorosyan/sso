package auth.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfirmValidator.class)
public @interface Confirm {

    String field();

    String confirm();

    String message() default "The field not confirmed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
