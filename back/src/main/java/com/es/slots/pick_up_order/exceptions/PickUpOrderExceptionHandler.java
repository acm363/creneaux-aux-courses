package com.es.slots.pick_up_order.exceptions;

import com.es.slots.pick_up_order.exceptions.customs.PickUpOrderAlreadyExistException;
import com.es.slots.pick_up_order.exceptions.customs.PickUpOrderNotFoundException;
import com.es.slots.global.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class PickUpOrderExceptionHandler {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PickUpOrderNotFoundException.class)
    /**
     * Handles the exception when a pick up order is not found
     * @param exception
     * @return an error message
     */
    public ErrorMessage handlePickUpOrderNotFoundException(PickUpOrderNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(exception.getMessage());
        errorMessage.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorMessage.setTimestamp(new Date());
        return errorMessage;
    }

    /**
     * Handles the exception when a pick up order already exists
     * @param exception
     * @return  an error message
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PickUpOrderAlreadyExistException.class)
    public ErrorMessage handlePickUpOrderAlreadyExistException(PickUpOrderAlreadyExistException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(exception.getMessage());
        errorMessage.setStatusCode(HttpStatus.CONFLICT.value());
        errorMessage.setTimestamp(new Date());
        return errorMessage;
    }
}
