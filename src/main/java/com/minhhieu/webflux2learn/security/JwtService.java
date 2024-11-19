package com.minhhieu.webflux2learn.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Log4j2
@Component
public class JwtService {
    private final JWTVerifier verifier;
    private final JWTCreator.Builder jwtCreator;
    private final Algorithm algorithm;

    public JwtService(@Value("${authentication.jwt.public-key}") String jwtPublicKey,
                      @Value("${authentication.jwt.private-key}") String jwtPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        algorithm = Algorithm.RSA256(loadPublicKey(jwtPublicKey), loadPrivateKey(jwtPrivateKey));
        verifier = JWT.require(algorithm).build();
        jwtCreator = JWT.create();
    }

    private RSAPublicKey loadPublicKey(String publicKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] decoded = Base64.getDecoder().decode(publicKeyPem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    private RSAPrivateKey loadPrivateKey(String privateKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] decoded = Base64.getDecoder().decode(privateKeyPem);
        PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(decoded);
        return (RSAPrivateKey) kf.generatePrivate(keySpecPv);
    }

    public String generateToken(Map<String, Object> claims) {
        return jwtCreator.withJWTId(UUID.randomUUID().toString())
                .withPayload(claims)
                .sign(algorithm);
    }

    public Authentication decode(String token) {
        try {
            var jwt = verifier.verify(token);
            Long id = jwt.getClaim("id").asLong();

            if (id == null || id < 0)
                return null;

            var principal = new AuthAccount()
                    .setId(id)
                    .setExp(jwt.getClaim("exp").asLong())
                    .setEmail(jwt.getClaim("email").asString())
                    .setIat(jwt.getClaim("iat").asLong())
                    .setJti(jwt.getId());
            var authentication = new UsernamePasswordAuthenticationToken(principal, token, List.of());
            authentication.setDetails(jwt);
            return authentication;
        } catch (JWTVerificationException e) {
            log.debug(e);
            return null;
        }
    }
}
