package com.es.slots.slot.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

import com.es.slots.slot.entities.ExceptionalSlot;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class SlotResponseDTO {

    @NotBlank(message = "Public id is required")
    private String publicId;

    @NotNull(message = "Start hour is required")
    @Schema(type = "String", pattern = "08:00")
    private LocalTime startHour;

    @NotNull(message = "End hour is required")
    @Schema(type = "String", pattern = "20:00")
    private LocalTime endHour;

    @NotNull(message = "Capacity is required")
    private int capacity;

    @NotNull(message = "currentCharge can not be null")
    private int currentCharge;

    @NotNull(message = "remainingCapacity can not be null")
    private int remainingCapacity;


}