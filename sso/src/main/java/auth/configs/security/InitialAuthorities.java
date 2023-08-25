package auth.configs.security;

public interface InitialAuthorities {
    String ROLE_ADMIN = "ROLE_ADMIN";
    String ROLE_USER = "ROLE_USER";

    static String[] admin() {
        return new String[]{ROLE_ADMIN};
    }

    static String[] user() {
        return new String[]{ROLE_USER};
    }
}
