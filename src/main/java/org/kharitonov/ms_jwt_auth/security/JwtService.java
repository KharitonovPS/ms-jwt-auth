package org.kharitonov.ms_jwt_auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.kharitonov.ms_jwt_auth.exceptions.JwtNotValidException;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.dto.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    private Map<String, String> claimMap = new HashMap<>();

    @PostConstruct
    void init() {
        jwtSigningKey = Base64.encodeBase64String(jwtSigningKey.getBytes());
    }

    private JWTVerifier getVerifier() {
        return JWT.require(Algorithm.HMAC256(jwtSigningKey))
                .withIssuer("jwt-auth")
                .build();
    }

    public JwtAuthenticationResponse generateToken(User user) {
        Map<String, String> claims = new HashMap<>();
        claims.put("id", String.valueOf(user.getId()));
        claims.put("email", user.getEmail());
        claims.put("role", String.valueOf(user.getRoles()));
        claims.put("username", user.getUsername());
        String token;
        try {
            token = JWT.create()
                    .withHeader("{\"alg\": \"HS256\", \"typ\": \"JWT\"}")
                    .withPayload(claims)
                    .withIssuedAt(Instant.now())
                    .withIssuer("jwt-auth")
                    .withExpiresAt(Instant.now().plusSeconds(60 * 60 * 24))
                    .sign(Algorithm.HMAC256(jwtSigningKey));
        } catch (JWTCreationException | IllegalArgumentException exception) {
            throw new JwtNotValidException(
                    "Неправильная конфигурация подписи / невозможно декодировать Claims");
        }
        return new JwtAuthenticationResponse(token);
    }

    public boolean isTokenValid(String jwtToken, User user) {
        final String userName = extractUsername(jwtToken);
        return (userName.equals(user.getUsername()) && !isTokenExpired());
    }

    public String extractUsername(String jwtToken) {
        if (claimMap.isEmpty()) {
            extractClaimFromToken(jwtToken);
        }
        return claimMap.get("username");
    }

    private boolean isTokenExpired() {
        Instant expiresAt = Instant.parse(claimMap.get("ExpiresAt"));
        return expiresAt.isBefore(Instant.now());
    }

    private void extractClaimFromToken(String jwtToken) {
        try {
            DecodedJWT decodedJWT = getVerifier().verify(jwtToken);
            Map<String, Claim> claims = decodedJWT.getClaims();
            if (claims != null && !claims.isEmpty()) {
                claims.forEach((k, v) -> {
                    claimMap.put(k, cutClaim(v));
                });
                claimMap.put("ExpiresAt", decodedJWT.getExpiresAt().toInstant().toString());
            }
        } catch (JWTVerificationException e) {
            throw new JwtNotValidException("Не верный JWT, пустые claims");
        }
    }

    private String cutClaim(Claim claim) {
        return claim.toString().substring(1, claim.toString().length() - 1);
    }
}
