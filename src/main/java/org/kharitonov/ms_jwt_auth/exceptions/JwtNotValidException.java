package org.kharitonov.ms_jwt_auth.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
public class JwtNotValidException extends RuntimeException{


    public JwtNotValidException(String message) {
        super(message);
    }
}
