package com.es.slots.pick_up_order.mapper;

import com.es.slots.pick_up_order.dto.response.PickUpOrderResponse;
import com.es.slots.pick_up_order.entities.PickUpOrder;
import com.es.slots.slot.dtos.responses.ExceptionalSlotResponseDTO;
import com.es.slots.slot.dtos.responses.StandardSlotResponseDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.entities.Slot;
import com.es.slots.slot.entities.StandardSlot;
import com.es.slots.slot.mapper.SlotMapper;
import com.es.slots.user.entities.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
class PickUpOrderMapperTest {

    @MockBean
    @Qualifier("slotMapper")
    private SlotMapper mockSlotMapper;

    @Autowired
    private PickUpOrderMapper pickUpOrderMapper;

    @SpyBean
    private PickUpOrderMapper spyPickUpOrderMapper;


    @Test
    void buildPickUpOrderResponseDTO_usingStandardSlot_shouldWorks() {
        Slot slot = new StandardSlot();

        String userUUID = UUID.randomUUID().toString();
        Client client = new Client();
        client.setPublicId(userUUID);
        StandardSlotResponseDTO standardSlotResponseDTO = new StandardSlotResponseDTO();

        String uuid = UUID.randomUUID().toString();
        PickUpOrder pickUpOrder = new PickUpOrder();
        pickUpOrder.setSlot(slot);
        pickUpOrder.setPickUpOrderId(uuid);
        pickUpOrder.setClient(client);

        when(mockSlotMapper.buildSlotDTO(slot)).thenReturn(standardSlotResponseDTO);

        PickUpOrderResponse pickUpOrderResponse = pickUpOrderMapper.buildPickUpOrderResponseDTO(pickUpOrder);
        PickUpOrderResponse pickUpOrderResponseWithAllArgs = new PickUpOrderResponse(uuid, standardSlotResponseDTO, userUUID);

        Assertions.assertEquals(pickUpOrder.getPickUpOrderId(), pickUpOrderResponse.getPickUpOrderId());
        Assertions.assertEquals(standardSlotResponseDTO, pickUpOrderResponse.getSlot());
        Assertions.assertEquals(client.getPublicId(), pickUpOrderResponse.getUserPublicId());
        Assertions.assertEquals(pickUpOrderResponseWithAllArgs, pickUpOrderResponse);



        verify(mockSlotMapper, times(1)).buildSlotDTO(slot);
    }

    @Test
    void buildPickUpOrderResponseDTO_usingExceptionalSlot_shouldWorks() {
        Slot slot = new ExceptionalSlot();
        Client client = new Client();
        client.setPublicId(UUID.randomUUID().toString());
        ExceptionalSlotResponseDTO exceptionalSlotResponseDTO = new ExceptionalSlotResponseDTO();

        PickUpOrder pickUpOrder = new PickUpOrder();
        pickUpOrder.setSlot(slot);
        pickUpOrder.setPickUpOrderId(UUID.randomUUID().toString());
        pickUpOrder.setClient(client);

        when(mockSlotMapper.buildSlotDTO(slot)).thenReturn(exceptionalSlotResponseDTO);

        PickUpOrderResponse pickUpOrderResponse = pickUpOrderMapper.buildPickUpOrderResponseDTO(pickUpOrder);
        Assertions.assertEquals(pickUpOrder.getPickUpOrderId(), pickUpOrderResponse.getPickUpOrderId());
        Assertions.assertEquals(exceptionalSlotResponseDTO, pickUpOrderResponse.getSlot());
        Assertions.assertEquals(client.getPublicId(), pickUpOrderResponse.getUserPublicId());

        verify(mockSlotMapper, times(1)).buildSlotDTO(slot);
    }

    @Test
    void buildPickUpOrderResponseDTOList() {
        List<PickUpOrder> pickUpOrderResponseList = new ArrayList<>();
        ExceptionalSlotResponseDTO exceptionalSlotResponseDTO = new ExceptionalSlotResponseDTO();

        for (int i = 0; i < 10; i++) {
            Slot slot = new ExceptionalSlot();
            Client client = new Client();
            client.setPublicId(UUID.randomUUID().toString());

            PickUpOrder pickUpOrder = new PickUpOrder();
            pickUpOrder.setSlot(slot);
            pickUpOrder.setPickUpOrderId(UUID.randomUUID().toString());
            pickUpOrder.setClient(client);
            pickUpOrderResponseList.add(pickUpOrder);
        }

        when(mockSlotMapper.buildSlotDTO(any())).thenReturn(exceptionalSlotResponseDTO);

        List<PickUpOrderResponse> pickUpOrderResponses = pickUpOrderMapper.buildPickUpOrderResponseDTOList(pickUpOrderResponseList);

        Assertions.assertEquals(10, pickUpOrderResponses.size());
        verify(spyPickUpOrderMapper, times(10)).buildPickUpOrderResponseDTO(any());

    }
}