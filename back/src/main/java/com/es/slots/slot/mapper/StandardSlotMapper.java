package com.es.slots.slot.mapper;

import com.es.slots.slot.dtos.requests.StandardSlotRequestDTO;
import com.es.slots.slot.dtos.responses.StandardSlotResponseDTO;
import com.es.slots.slot.entities.StandardSlot;
import org.springframework.stereotype.Component;

@Component
public class StandardSlotMapper extends SlotMapper {

    /**
     * Create a new instance of StandardSlot with request's data
     *
     * @param standardSlotRequestDTO Data requests
     * @return a new StandardSlot
     */
    public StandardSlot updateStandardSlotFromRequest(StandardSlot standardSlot, StandardSlotRequestDTO standardSlotRequestDTO) {
        super.buildSlotFromRequest(standardSlot, standardSlotRequestDTO);
        standardSlot.setDayOfWeek(standardSlotRequestDTO.getDayOfWeek());
        return standardSlot;
    }


    /**
     * Build SlotStandardResponseResponseDTO from StandardSlot
     *
     * @param standardSlot The instance to transform
     * @return the response with instance's data
     */
    public StandardSlotResponseDTO buildResponseFromStandardSlot(StandardSlot standardSlot) {
        StandardSlotResponseDTO slotResponseDTO = (StandardSlotResponseDTO) super.buildSlotDTO(standardSlot);
        slotResponseDTO.setDayOfWeek(standardSlot.getDayOfWeek());
        return slotResponseDTO;
    }

}
