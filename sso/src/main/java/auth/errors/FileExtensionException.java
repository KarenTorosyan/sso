package auth.errors;

public class FileExtensionException extends ResourceBundleRuntimeException {

    public FileExtensionException(String message, String code, Object... arguments) {
        super(message, code, arguments);
    }
}
