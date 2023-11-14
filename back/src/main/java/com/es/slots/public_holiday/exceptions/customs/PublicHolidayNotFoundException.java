package com.es.slots.public_holiday.exceptions.customs;

// Represents a public holiday not found exception.
public class PublicHolidayNotFoundException extends Exception {

    public PublicHolidayNotFoundException(String publicId) {
        super("Le jour férié avec l'id " + publicId + " n'existe pas");
    }

}
