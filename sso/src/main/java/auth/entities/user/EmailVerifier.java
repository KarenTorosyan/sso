package auth.entities.user;

@FunctionalInterface
public interface EmailVerifier {
    void verify(Email email);
}