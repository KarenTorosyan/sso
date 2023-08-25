package auth.validators;

import auth.utils.ReflectionUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmValidator implements ConstraintValidator<Confirm, Object> {

    private String field;
    private String confirm;
    private String message;

    @Override
    public void initialize(Confirm constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.confirm = constraintAnnotation.confirm();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object fieldValue = ReflectionUtils.getDeclaredFieldValue(value, field);
            Object confirmValue = ReflectionUtils.getDeclaredFieldValue(value, confirm);

            if (fieldValue.equals(confirmValue)) {
                return true;
            }
            buildConstraintViolation(context);
            return false;

        } catch (RuntimeException e) {
            buildConstraintViolation(context);
            return false;
        }
    }

    private void buildConstraintViolation(ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(this.confirm)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}
