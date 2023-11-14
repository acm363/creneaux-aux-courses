package com.es.slots.pick_up_order.mapper;

import com.es.slots.pick_up_order.dto.response.PickUpOrderResponse;
import com.es.slots.pick_up_order.entities.PickUpOrder;
import com.es.slots.slot.mapper.SlotMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PickUpOrderMapper {

    private SlotMapper slotMapper;

    public PickUpOrderResponse buildPickUpOrderResponseDTO(PickUpOrder pickUpOrder) {
        PickUpOrderResponse pickUpOrderResponse = new PickUpOrderResponse();
        pickUpOrderResponse.setPickUpOrderId(pickUpOrder.getPickUpOrderId());
        pickUpOrderResponse.setSlot(slotMapper.buildSlotDTO(pickUpOrder.getSlot()));
        pickUpOrderResponse.setUserPublicId(pickUpOrder.getClient().getPublicId());
        return pickUpOrderResponse;
    }

    public List<PickUpOrderResponse> buildPickUpOrderResponseDTOList(List<PickUpOrder> pickUpOrders) {
        return pickUpOrders.stream().map(this::buildPickUpOrderResponseDTO).toList();
    }

}
