package com.es.slots.slot.exceptions;

import com.es.slots.global.ErrorMessage;
import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
import com.es.slots.slot.exceptions.customs.FullSlotException;
import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class SlotCustomExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SlotNotFoundException.class)
    /**
     * Handle SlotNotFoundException
     * @param exception
     * @return ErrorMessage
     */
    public ErrorMessage handleSlotNotFoundException(SlotNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(exception.getMessage());
        errorMessage.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorMessage.setTimestamp(new Date());
        return errorMessage;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SlotValidityFailureException.class)
    /**
     * Handle SlotValidityFailureException
     * @param exception
     * @return ErrorMessage
     */
    public ErrorMessage handleSlotCreationFailureException(SlotValidityFailureException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(exception.getMessage());
        errorMessage.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorMessage.setTimestamp(new Date());
        return errorMessage;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(FullSlotException.class)
    /**
     * Handle FullSlotException
     * @param exception
     * @return ErrorMessage
     */
    public ErrorMessage handleFullSlotException(FullSlotException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(exception.getMessage());
        errorMessage.setStatusCode(HttpStatus.CONFLICT.value());
        errorMessage.setTimestamp(new Date());
        return errorMessage;
    }
}
