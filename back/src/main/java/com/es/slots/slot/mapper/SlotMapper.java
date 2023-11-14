package com.es.slots.slot.mapper;

import com.es.slots.slot.dtos.requests.SlotRequestDTO;
import com.es.slots.slot.dtos.responses.ExceptionalSlotResponseDTO;
import com.es.slots.slot.dtos.responses.SlotResponseDTO;
import com.es.slots.slot.dtos.responses.StandardSlotResponseDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.entities.Slot;
import com.es.slots.slot.entities.StandardSlot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SlotMapper {

    /**
     * Build a SlotResponseDTO from a Slot
     * @param slot the slot
     * @return the SlotResponseDTO
     */
    public SlotResponseDTO buildSlotDTO(Slot slot) {
        if (slot instanceof ExceptionalSlot exceptionalSlot) {
            return getExceptionalSlotDto(exceptionalSlot);
        }
        else {
            StandardSlot standardSlot = (StandardSlot) slot;
            return getStandardSlotDto(standardSlot);
        }
    }


    /**
     * Build a SlotResponseDTO from a ExceptionalSlot
     * @param exceptionalSlot the exceptional slot
     * @return the SlotResponseDTO
     */
    private ExceptionalSlotResponseDTO getExceptionalSlotDto(ExceptionalSlot exceptionalSlot) {
        ExceptionalSlotResponseDTO slotExceptionalDto = new ExceptionalSlotResponseDTO();
        slotExceptionalDto.setPublicId(exceptionalSlot.getPublicId());
        slotExceptionalDto.setStartHour(exceptionalSlot.getStartHour());
        slotExceptionalDto.setEndHour(exceptionalSlot.getEndHour());
        slotExceptionalDto.setDate(exceptionalSlot.getDayDate());
        slotExceptionalDto.setCapacity(exceptionalSlot.getCapacity());

        int slotCurrentCharge = exceptionalSlot.getPickUpOrderList().size();
        slotExceptionalDto.setCurrentCharge(slotCurrentCharge);
        slotExceptionalDto.setRemainingCapacity(exceptionalSlot.getCapacity() - slotCurrentCharge);
        return slotExceptionalDto;
    }


    /**
     * Build a SlotResponseDTO from a StandardSlot
     * @param standardSlot the standard slot
     * @return the SlotResponseDTO
     */
    private StandardSlotResponseDTO getStandardSlotDto(Slot standardSlot) {
        StandardSlotResponseDTO standardSlotResponseDto = new StandardSlotResponseDTO();
        standardSlotResponseDto.setPublicId(standardSlot.getPublicId());
        standardSlotResponseDto.setStartHour(standardSlot.getStartHour());
        standardSlotResponseDto.setEndHour(standardSlot.getEndHour());
        standardSlotResponseDto.setCapacity(standardSlot.getCapacity());
        standardSlotResponseDto.setDayOfWeek(((StandardSlot) standardSlot).getDayOfWeek());

        int slotCurrentCharge = standardSlot.getPickUpOrderList().size();
        standardSlotResponseDto.setCurrentCharge(slotCurrentCharge);
        standardSlotResponseDto.setRemainingCapacity(standardSlot.getCapacity() - slotCurrentCharge);
        return standardSlotResponseDto;
    }


    /**
     * Build a Slot from a SlotRequestDTO
     *
     * @param slot           the slot to update
     * @param slotRequestDTO the slot request dto
     */
    public void buildSlotFromRequest(Slot slot, SlotRequestDTO slotRequestDTO) {
        slot.setStartHour(slotRequestDTO.getStartHour());
        slot.setEndHour(slotRequestDTO.getEndHour());
        slot.setCapacity(slotRequestDTO.getCapacity());
        slot.setPickUpOrderList(new ArrayList<>());
    }

}
