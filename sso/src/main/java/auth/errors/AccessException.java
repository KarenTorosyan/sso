package auth.errors;

public class AccessException extends ResourceBundleRuntimeException {

    public AccessException(String message, String code, Object... arguments) {
        super(message, code, arguments);
    }
}
