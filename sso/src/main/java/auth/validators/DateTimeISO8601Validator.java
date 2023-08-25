package auth.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class DateTimeISO8601Validator implements ConstraintValidator<ISO8601, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if (value != null) {
                Instant.parse(value);
            }
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
