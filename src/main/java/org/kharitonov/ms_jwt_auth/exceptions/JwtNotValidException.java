package org.kharitonov.ms_jwt_auth.exceptions;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
public class JwtNotValidException extends RuntimeException {


    public JwtNotValidException(String message) {
        super(message);
    }
}
