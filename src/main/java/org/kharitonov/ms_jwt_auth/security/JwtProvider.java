package org.kharitonov.ms_jwt_auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.kharitonov.ms_jwt_auth.exceptions.JwtNotValidException;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.dto.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Service
public class JwtProvider {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @PostConstruct
    void init() {
        jwtSigningKey = Base64.encodeBase64String(jwtSigningKey.getBytes());
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtSigningKey);
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
                    .withHeader("{\n  \"alg\": \"HS256\",\n  \"typ\": \"JWT\"\n}")
                    .withClaim("claims", claims)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(60 * 60 * 24))
                    .sign(getAlgorithm());
        } catch (JWTCreationException | IllegalArgumentException exception) {
            throw new JwtNotValidException(
                    "Invalid Signing configuration / Couldn't convert Claims.",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return new JwtAuthenticationResponse(token);
    }

    public String extractRole(String jwtToken) {
        DecodedJWT decodedJWT = extractClaimFromToken(jwtToken);
        return decodedJWT.getClaim("role").asString();
    }


    public boolean isTokenValid(String jwtToken, User user) {
        final String userName = extractUsername(jwtToken);
        return (userName.equals(user.getUsername()) && !isTokenExpired(jwtToken));
    }


    private String extractUsername(String jwtToken) {
        DecodedJWT decodedJWT = extractClaimFromToken(jwtToken);
        return decodedJWT.getClaim("username").asString();
    }

    private boolean isTokenExpired(String jwtToken) {
        DecodedJWT decodedJWT = extractClaimFromToken(jwtToken);
        Date expiresAt = decodedJWT.getExpiresAt();
        return expiresAt.before(new Date());
    }

    private JWTVerifier getVerifier() {
        return JWT.require(this.getAlgorithm())
                .withIssuer(jwtSigningKey)
                .build();
    }

    private DecodedJWT extractClaimFromToken(String jwtToken) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = getVerifier().verify(jwtToken);
        } catch (JWTVerificationException e) {
            throw new JwtNotValidException(
                    "Invalid Signing configuration or JWT was expired",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return decodedJWT;
    }
}
