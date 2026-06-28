package org.bigBrotherBooks.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;
import org.bigBrotherBooks.logger.LogType;
import org.bigBrotherBooks.logger.Logger;
import org.bigBrotherBooks.logger.LoggerFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bigBrotherBooks.constants.GlobalConstants.JWT_VALID_DURATION;

@Singleton
public class TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    private String ISSUER;
    @ConfigProperty(name = "smallrye.jwt.sign.key-location")
    private String KEY_LOCATION;

    public String generateToken(String userName, Set<String> roles) {
        try {
            RSAPrivateKey privateKey = getPrivateKey();
            return Jwt.issuer(ISSUER)
                    .subject(userName)   // will be taking only one of both
                    .upn(userName)
                    .groups(roles)
                    .issuedAt(System.currentTimeMillis() / 1000)
                    .expiresAt(System.currentTimeMillis() / 1000 + JWT_VALID_DURATION)
                    .sign(privateKey);
        } catch (Exception e) {
            LOGGER.log(LogType.ERROR, "Failed to generate JWT: {}", e.getMessage());
            return null;
        }
    }

    private RSAPrivateKey getPrivateKey() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(KEY_LOCATION)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String privateKeyContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));

            String privateKeyPEM = privateKeyContent
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "");

            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            LOGGER.log(LogType.ERROR, "Failed to load JWT signing key from {}: {}", KEY_LOCATION, e.getMessage());
            return null;
        }
    }
}
