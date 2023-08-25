package auth.validator;

import auth.validators.Confirm;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmValidatorTest {

    private static final String FIELD_NOT_CONFIRMED_MESSAGE = "The password not confirmed!";

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Confirm(field = "password", confirm = "passwordConfirm", message = FIELD_NOT_CONFIRMED_MESSAGE)
    record ValidateMe(String password, String passwordConfirm) {
    }

    @Test
    void shouldReturnEmptyErrorMessagesWhenFieldConfirmed() {
        Set<ConstraintViolation<ValidateMe>> constraintViolations = validator.validate(new ValidateMe("pass", "pass"));
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void shouldGetExpectedErrorMessageWhenFieldNotConfirmed() {
        Set<ConstraintViolation<ValidateMe>> constraintViolations = validator.validate(new ValidateMe("pass", "pas"));
        List<String> messages = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        assertThat(messages)
                .contains(FIELD_NOT_CONFIRMED_MESSAGE)
                .hasSize(1);
    }
}
