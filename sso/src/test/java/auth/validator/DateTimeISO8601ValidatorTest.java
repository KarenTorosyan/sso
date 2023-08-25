package auth.validator;

import auth.validators.ISO8601;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTimeISO8601ValidatorTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    record ValidateMe(@ISO8601 String date) {
    }

    @Test
    void shouldReturnEmptyErrorMessagesWhenValueIsValid() {
        Set<ConstraintViolation<ValidateMe>> constraintViolations = validator.validate(new ValidateMe(String.valueOf(Instant.now())));
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void shouldGetExpectedErrorMessageWhenValueInvalid() {
        Set<ConstraintViolation<ValidateMe>> constraintViolations = validator.validate(new ValidateMe("invalid date format"));
        List<String> messages = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        assertThat(messages)
                .contains(ISO8601.DEFAULT_MESSAGE)
                .hasSize(1);
    }
}
