package auth.entities.user;

@FunctionalInterface
public interface EmailVerifier {
    void verify(String baseUrl, Email email);
}
