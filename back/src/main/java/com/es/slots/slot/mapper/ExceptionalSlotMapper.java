package com.es.slots.slot.mapper;

import com.es.slots.slot.dtos.requests.ExceptionalSlotRequestDTO;
import com.es.slots.slot.dtos.responses.ExceptionalSlotResponseDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import org.springframework.stereotype.Component;

@Component
public class ExceptionalSlotMapper extends SlotMapper {

    /**
     * Create a new instance of StandardSlot with request's data
     *
     * @param exceptionalSlotRequestDTO Data requests
     * @return a new ExceptionalSlot
     */
    public ExceptionalSlot buildExceptionalSlotFromRequest(ExceptionalSlot exceptionalSlot, ExceptionalSlotRequestDTO exceptionalSlotRequestDTO) {
        super.buildSlotFromRequest(exceptionalSlot, exceptionalSlotRequestDTO);
        exceptionalSlot.setDayDate(exceptionalSlotRequestDTO.getDate());
        return exceptionalSlot;
    }

    /**
     * Build SlotExceptionalResponseDTO from StandardSlot
     *
     * @param exceptionalSlot The instance to transform
     * @return the response with instance's data
     */
    public ExceptionalSlotResponseDTO buildResponseFromExceptionalSlot(ExceptionalSlot exceptionalSlot) {
        ExceptionalSlotResponseDTO slotResponseDTO = (ExceptionalSlotResponseDTO) super.buildSlotDTO(exceptionalSlot);
        slotResponseDTO.setDate(exceptionalSlot.getDayDate());
        return slotResponseDTO;
    }


}
