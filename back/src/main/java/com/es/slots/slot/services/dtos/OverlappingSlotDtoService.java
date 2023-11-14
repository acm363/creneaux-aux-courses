package com.es.slots.slot.services.dtos;

import com.es.slots.public_holiday.services.PublicHolidayService;
import com.es.slots.slot.dtos.responses.OverlappingSlotResponseDTO;
import com.es.slots.slot.dtos.responses.SlotResponseDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.entities.Slot;
import com.es.slots.slot.entities.StandardSlot;
import com.es.slots.slot.mapper.ExceptionalSlotMapper;
import com.es.slots.slot.mapper.StandardSlotMapper;
import com.es.slots.slot.services.ExceptionalSlotService;
import com.es.slots.slot.services.StandardSlotService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OverlappingSlotDtoService {

    private final StandardSlotService standardSlotService;
    private final StandardSlotMapper standardSlotMapper;
    private final ExceptionalSlotService exceptionalSlotService;
    private final ExceptionalSlotMapper exceptionalSlotMapper;
    private final PublicHolidayService publicHolidayService;


    /**
     * Get the overlapping slot response DTO.
     * @param slot the slot
     * @return the overlapping slot response DTO.
     */
    public OverlappingSlotResponseDTO getOverlappingSlotResponseDTO(Slot slot) {
        LocalDate slotDate;
        DayOfWeek slotDay;
        SlotResponseDTO slotResponseDTO;

        if (slot instanceof ExceptionalSlot exceptionalSlot) {
            slotDate = exceptionalSlot.getDayDate();
            slotDay = slotDate.getDayOfWeek();
            slotResponseDTO = exceptionalSlotMapper.buildResponseFromExceptionalSlot(exceptionalSlot);
        }
        else {
            slotDay = ((StandardSlot) slot).getDayOfWeek();
            slotDate = findLocalDateOfNextSpecificDayOfWeek(slotDay);
            slotResponseDTO = standardSlotMapper.buildResponseFromStandardSlot((StandardSlot) slot);
        }

        List<ExceptionalSlot> exceptionalSlots = exceptionalSlotService.getAllByDate(slotDate);
        List<Slot> overlappingSlots = new ArrayList<>();

        for (ExceptionalSlot exceptionalSlot_ : exceptionalSlots) {
            if (!exceptionalSlot_.equals(slot) && this.isOverlapping(slot, exceptionalSlot_)) {
                overlappingSlots.add(exceptionalSlot_);
            }
        }

        boolean dateIsAnHoliday = publicHolidayService.isPublicHoliday(slotDate);
        if (!dateIsAnHoliday) {
            List<StandardSlot> standardSlots = standardSlotService.getAllByDay(slotDay);
            for (StandardSlot standardSlot_ : standardSlots) {
                if (!standardSlot_.equals(slot) && this.isOverlapping(slot, standardSlot_)) {
                    overlappingSlots.add(standardSlot_);
                }
            }
        }

        List<SlotResponseDTO> slotResponseDTOS = overlappingSlots.stream().map(this::buildSlotResponseDto).toList();

        OverlappingSlotResponseDTO overlappingSlotResponseDTO = new OverlappingSlotResponseDTO();
        overlappingSlotResponseDTO.setSlotDto(slotResponseDTO);
        overlappingSlotResponseDTO.setOverlappingSlots(slotResponseDTOS);
        return overlappingSlotResponseDTO;
    }


    /**
     * Find the next date of a specific day of week.
     * @param dayOfWeek the day of week
     * @return the next date of the specific day of week.
     */
    private LocalDate findLocalDateOfNextSpecificDayOfWeek(DayOfWeek dayOfWeek) {
        LocalDate today = LocalDate.now();
        return today.datesUntil(today.plusDays(7))
                .filter(localDate -> localDate.getDayOfWeek().getValue() == dayOfWeek.getValue())
                .findFirst().orElseThrow();
    }


    /**
     * Check if two slot are overlapping.
     * @param slot1 one slot
     * @param slot2 another slot
     * @return true if two slot are overlapping.
     */
    private boolean isOverlapping(Slot slot1, Slot slot2) {
        return slot1.getStartHour().isBefore(slot2.getEndHour()) && slot2.getStartHour().isBefore(slot1.getEndHour());
    }


    private SlotResponseDTO buildSlotResponseDto(Slot slot) {
        if (slot instanceof ExceptionalSlot) {
            return exceptionalSlotMapper.buildResponseFromExceptionalSlot((ExceptionalSlot) slot);
        }
        else {
            return standardSlotMapper.buildResponseFromStandardSlot((StandardSlot) slot);
        }
    }

}
