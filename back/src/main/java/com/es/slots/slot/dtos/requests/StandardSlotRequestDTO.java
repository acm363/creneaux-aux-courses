package com.es.slots.slot.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardSlotRequestDTO extends SlotRequestDTO {

    @NotNull(message = "dayOfWeek is required")
    private DayOfWeek dayOfWeek;

}
