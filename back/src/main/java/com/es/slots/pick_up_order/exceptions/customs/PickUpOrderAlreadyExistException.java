package com.es.slots.pick_up_order.exceptions.customs;

public class PickUpOrderAlreadyExistException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     * @param message
     */
    public PickUpOrderAlreadyExistException(String message) {
        super(message);
    }

}
