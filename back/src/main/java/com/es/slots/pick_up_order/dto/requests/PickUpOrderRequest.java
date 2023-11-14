package com.es.slots.pick_up_order.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickUpOrderRequest {

    @NotNull(message = "Date is required")
    @Schema(type = "String", pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Slot public id is required")
    private String slotId;

}
