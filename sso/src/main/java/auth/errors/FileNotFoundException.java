package auth.errors;

public class FileNotFoundException extends ResourceBundleRuntimeException {

    public FileNotFoundException(String message, String code, Object... arguments) {
        super(message, code, arguments);
    }
}
