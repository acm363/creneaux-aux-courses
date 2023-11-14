package com.es.slots.authentication.exceptions;

import com.es.slots.global.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    /**
     * Exception handler for {@link BadCredentialsException}.
     * Handles and transforms a {@link BadCredentialsException} into an {@link ErrorMessage} with
     * an UNAUTHORIZED (401) status code, the exception message, and a timestamp.
     *
     * @param exception The {@link BadCredentialsException} to handle.
     * @return An {@link ErrorMessage} object with the error details.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorMessage handleBadCredentialsException(BadCredentialsException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(exception.getMessage());
        errorMessage.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorMessage.setTimestamp(new Date());
        return errorMessage;
    }
    /**
     * Exception handler for {@link AccessDeniedException}.
     * Handles and transforms an {@link AccessDeniedException} into an {@link ErrorMessage} with
     * a FORBIDDEN (403) status code, the exception message, and a timestamp.
     *
     * @param exception The {@link AccessDeniedException} to handle.
     * @return An {@link ErrorMessage} object with the error details.
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorMessage handleAccessDeniedException(AccessDeniedException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(exception.getMessage());
        errorMessage.setStatusCode(HttpStatus.FORBIDDEN.value());
        errorMessage.setTimestamp(new Date());
        return errorMessage;
    }
}
