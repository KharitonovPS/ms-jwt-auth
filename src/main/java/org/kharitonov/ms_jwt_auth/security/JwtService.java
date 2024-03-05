package org.kharitonov.ms_jwt_auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.kharitonov.ms_jwt_auth.exceptions.JwtNotValidException;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.UserRole;
import org.kharitonov.ms_jwt_auth.model.dto.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.kharitonov.ms_jwt_auth.model.UserRole.fromString;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

//    @PostConstruct
//    void init() {
//        jwtSigningKey = Base64.encodeBase64String(jwtSigningKey.getBytes());
//    }

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
                    "Invalid Signing configuration / Couldn't convert Claims.",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return new JwtAuthenticationResponse(token);
    }

    public String extractUsername(String jwtToken) {
        Map<String, String> claimMap = extractClaimFromToken(jwtToken);
        return claimMap.get("username");
    }

//    public String extractUserName(String jwtToken) {
//        DecodedJWT decodedJWT = extractClaimFromToken(jwtToken);
//        return decodedJWT.getClaim("username").asString();
//    }


    public boolean isTokenValid(String jwtToken, User user) {
        final String userName = extractUsername(jwtToken);
        Map<String, String> claimMap = extractClaimFromToken(jwtToken);
        return (userName.equals(user.getUsername()) && !isTokenExpired(claimMap));
    }


    public UserRole extractRole(String jwtToken) {
        Map<String, String> stringMap = extractClaimFromToken(jwtToken);
        UserRole role;
        try {
            role = fromString(stringMap.get("role"));
        } catch (NullPointerException e){
            //null role=[ROLE_USER]
            role =UserRole.ROLE_UNAUTHORIZED;
        }
        return role;
    }

    private boolean isTokenExpired(Map<String, String> map) {
        Instant expiresAt = Instant.parse(map.get("ExpiresAt"));
//        Data expiresAt = map.get("eat").toString();
        return expiresAt.isBefore(Instant.now());
//        return true;
    }

    private JWTVerifier getVerifier() {
        return JWT.require(Algorithm.HMAC256(jwtSigningKey))
                .withIssuer("jwt-auth")
                .build();
    }


    protected Map<String, String> extractClaimFromToken(String jwtToken) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            DecodedJWT decodedJWT = getVerifier().verify(jwtToken);
            Map<String, Claim> claims = decodedJWT.getClaims();
            Map<String, Object> payloadMap = claims.get("claims").asMap();
            payloadMap.forEach((k, v) -> {
                resultMap.put(k, v.toString());
            });
            resultMap.put("ExpiresAt", decodedJWT.getExpiresAt().toInstant().toString());
        } catch (JWTVerificationException e) {
            throw new JwtNotValidException(
                    "Invalid Signing configuration or JWT was expired",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return resultMap;
    }
}
