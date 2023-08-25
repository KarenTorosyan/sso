package auth.errors;

public class ConfirmationException extends ResourceBundleRuntimeException {

    public ConfirmationException(String message, String code, Object... arguments) {
        super(message, code, arguments);
    }
}
