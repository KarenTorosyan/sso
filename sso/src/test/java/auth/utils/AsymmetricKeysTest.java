package auth.utils;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AsymmetricKeysTest {

    private static final int RSA_PRIVATE_KEY_SIZE = 2048;

    private static final Curve ECDSA_CURVE = Curve.P_256;

    private static final int ECDSA_PRIVATE_KEY_SIZE = 256;

    @Test
    void shouldGenerateRsa256KeyWithExpectedPrivateKeySize() {
        assertThat(AsymmetricKeys.rsa(RSA_PRIVATE_KEY_SIZE))
                .isNotNull()
                .isExactlyInstanceOf(RSAKey.class);
    }

    @Test
    void shouldGenerateEcdsaKeyWithExpectedCurveAndPrivateKeySize() {
        assertThat(AsymmetricKeys.ecdsa(ECDSA_CURVE, ECDSA_PRIVATE_KEY_SIZE))
                .isNotNull()
                .isExactlyInstanceOf(ECKey.class);
    }
}
