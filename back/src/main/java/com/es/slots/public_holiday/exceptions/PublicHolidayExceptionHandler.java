package com.es.slots.public_holiday.exceptions;

import com.es.slots.global.ErrorMessage;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayNotFoundException;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice

// Represents a public holiday exception handler.
public class PublicHolidayExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PublicHolidayNotFoundException.class)
    /**
     * Handle public holiday not found exception.
     * @param exception
     * @return ErrorMessage
     */
    public ErrorMessage handlePublicHolidayNotFoundException(PublicHolidayNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(exception.getMessage());
        errorMessage.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorMessage.setTimestamp(new Date());
        return errorMessage;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PublicHolidayAlreadyExistException.class)
    /**
     * Handle public holiday already exist exception.
     * @param exception
     * @return  ErrorMessage
     */
    public ErrorMessage handlePublicHolidayUpdateFailureException(PublicHolidayAlreadyExistException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(exception.getMessage());
        errorMessage.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorMessage.setTimestamp(new Date());
        return errorMessage;
    }
}
