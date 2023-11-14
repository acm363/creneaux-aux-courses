package com.es.slots.slot.dtos.responses;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OverlappingSlotResponseDTO {
    @NotBlank(message = "SlotDto is required")
    private SlotResponseDTO slotDto;

    @NotBlank(message = "overlappingSlots must be at least empty")
    private List<SlotResponseDTO> overlappingSlots;
}
