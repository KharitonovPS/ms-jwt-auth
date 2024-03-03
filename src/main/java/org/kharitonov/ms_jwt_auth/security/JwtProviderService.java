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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Service
public class JwtProviderService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @PostConstruct
    void init() {
        jwtSigningKey = Base64.encodeBase64String(jwtSigningKey.getBytes());
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

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtSigningKey);
    }

    private JWTVerifier getVerifier() {
        return JWT.require(this.getAlgorithm())
                .withIssuer(jwtSigningKey)
                .build();
    }

    public boolean verify(String jwtToken) {
        // декодим в трай кеч
        DecodedJWT decodedJWT;
        try {
            decodedJWT = getVerifier().verify(jwtToken);
        } catch (JWTVerificationException e) {
            throw new JwtNotValidException(
                    "Invalid Signing configuration or JWT out of data.",
                    HttpStatus.UNAUTHORIZED
            );
        }
        //вытащили клейм и конверт в стрингу
        Claim claim = decodedJWT.getClaim("role");
        String role = claim.asString();

        //осталось настроить фильтры с верифаером
        return true;
    }
}
