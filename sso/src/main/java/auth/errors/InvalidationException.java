package auth.errors;

public class InvalidationException extends ResourceBundleRuntimeException {

    public InvalidationException(String message, String code, Object... arguments) {
        super(message, code, arguments);
    }
}
