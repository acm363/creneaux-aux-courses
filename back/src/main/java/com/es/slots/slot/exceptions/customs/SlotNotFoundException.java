package com.es.slots.slot.exceptions.customs;

public class SlotNotFoundException extends Exception {
    public SlotNotFoundException(String publicId) {
        super("Le créneau d'id " + publicId + " n'existe pas");
    }
}
