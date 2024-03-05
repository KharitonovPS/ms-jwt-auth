package org.kharitonov.ms_jwt_auth.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Component
public class BasePasswordEncoder {

    private final static Base64 BASE_64 = new Base64();

    public String encode(String password) {
        return BASE_64.encodeAsString(password.getBytes(StandardCharsets.UTF_8));
    }
}
