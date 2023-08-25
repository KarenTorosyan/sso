package auth.utils;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.SneakyThrows;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;

public class AsymmetricKeys {

    @SneakyThrows
    public static RSAKey rsa(int privateKeySize) {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(privateKeySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .build();
    }

    @SneakyThrows
    public static ECKey ecdsa(Curve curve, int privateKeySize) {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(privateKeySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new ECKey.Builder(curve, (ECPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .build();
    }
}
