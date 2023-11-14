package com.es.slots.slot.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class SlotRequestDTO {
    @NotNull(message = "Start hour is required")
    @Schema(type = "String", pattern = "08:00")
    private LocalTime startHour;

    @NotNull(message = "End hour is required")
    @Schema(type = "String", pattern = "20:00")
    private LocalTime endHour;

    @Digits(integer = 5, fraction = 0, message = "Capacity must be a number")
    @Min(value = 1, message = "Capacity must be greater than 0")
    @NotNull(message = "Capacity is required")
    private int capacity;
}
