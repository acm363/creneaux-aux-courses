package com.es.slots.pick_up_order.dto.response;

import com.es.slots.slot.dtos.responses.SlotResponseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// Represents a pick up order response
public class PickUpOrderResponse {

    @NotNull(message = "PickUpOrder public id is required")
    private String pickUpOrderId;

    @NotNull(message = "Date is required")
    private SlotResponseDTO slot;

    @NotNull(message = "Date is required")
    private String userPublicId;

}
