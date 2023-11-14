package com.es.slots.slot.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExceptionalSlotResponseDTO extends SlotResponseDTO {

    @NotNull(message = "Date is required")
    @Schema(type = "String", pattern = "yyyy-MM-dd")
    private LocalDate date;

}
