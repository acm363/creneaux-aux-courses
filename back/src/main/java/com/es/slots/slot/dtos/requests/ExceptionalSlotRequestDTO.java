package com.es.slots.slot.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor


public class ExceptionalSlotRequestDTO extends SlotRequestDTO {

    @NotNull(message = "Date is required")
    @Schema(type = "String", pattern = "yyyy-MM-dd")
    private LocalDate date;

}
