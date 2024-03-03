package org.kharitonov.ms_jwt_auth.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
public class JwtNotValidException extends RuntimeException{

    HttpStatus httpStatus;

    public JwtNotValidException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
