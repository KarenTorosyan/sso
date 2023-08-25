package auth.errors;

public class NotExistsException extends ResourceBundleRuntimeException {

    public NotExistsException(String message, String code, Object... arguments) {
        super(message, code, arguments);
    }
}
