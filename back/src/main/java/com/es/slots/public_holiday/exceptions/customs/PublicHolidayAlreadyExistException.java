package com.es.slots.public_holiday.exceptions.customs;

// Represents a public holiday already exist exception.
public class PublicHolidayAlreadyExistException extends Exception {

    public PublicHolidayAlreadyExistException() {
        super("Le jour férié existe déjà");
    }
}
