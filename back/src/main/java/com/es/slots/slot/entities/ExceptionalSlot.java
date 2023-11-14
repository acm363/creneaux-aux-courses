package com.es.slots.slot.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("EXCEPTIONAL")
@AllArgsConstructor

// ExceptionalSlot is a slot that is not recurring
public class ExceptionalSlot extends Slot {
    private LocalDate dayDate;
}
