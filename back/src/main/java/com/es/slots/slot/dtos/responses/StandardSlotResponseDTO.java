package com.es.slots.slot.dtos.responses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StandardSlotResponseDTO extends SlotResponseDTO {

    @NotNull(message = "dayOfWeek is required")
    private DayOfWeek dayOfWeek;

}
