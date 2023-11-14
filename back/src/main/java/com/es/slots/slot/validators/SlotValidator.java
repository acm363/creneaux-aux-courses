package com.es.slots.slot.validators;

import com.es.slots.slot.dtos.requests.SlotRequestDTO;
import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SlotValidator {

    private final LocalTime MIN_START_HOUR = LocalTime.of(8, 0);
    private final LocalTime MAX_END_HOUR = LocalTime.of(20, 0);
    private final int MIN_CAPACITY = 0;


    /**
     * Verify that the SlotRequest contains valid data
     * @param slotRequestDTO The SlotRequest to check
     * @return If the SlotRequest contains valid data
     * @throws SlotValidityFailureException If the SlotRequest contains invalid data
     */
    public boolean checkValidityOfSlotRequest(SlotRequestDTO slotRequestDTO) throws SlotValidityFailureException {
        return this.checkValidityOfHours(slotRequestDTO)
                && this.checkValidityOfStartHour(slotRequestDTO)
                && this.checkValidityOfEndHour(slotRequestDTO)
                && this.checkValidityOfCapacity(slotRequestDTO);
    }

    private boolean checkValidityOfHours(SlotRequestDTO slotRequestDTO) throws SlotValidityFailureException {
        if (slotRequestDTO.getStartHour().isAfter(slotRequestDTO.getEndHour())) {
            throw new SlotValidityFailureException("startHour must be before endHour");
        }
        return true;
    }

    private boolean checkValidityOfStartHour(SlotRequestDTO slotRequestDTO) throws SlotValidityFailureException {
        if (slotRequestDTO.getStartHour().isBefore(MIN_START_HOUR)) {
            throw new SlotValidityFailureException("startHour must start after " + MIN_START_HOUR);
        }
        return true;
    }

    private boolean checkValidityOfEndHour(SlotRequestDTO slotRequestDTO) throws SlotValidityFailureException {
        if (slotRequestDTO.getEndHour().isAfter(MAX_END_HOUR)) {
            throw new SlotValidityFailureException("endHour must end before " + MAX_END_HOUR);
        }
        return true;
    }

    private boolean checkValidityOfCapacity(SlotRequestDTO slotRequestDTO) throws SlotValidityFailureException {
        if (slotRequestDTO.getCapacity() <= MIN_CAPACITY) {
            throw new SlotValidityFailureException("capacity must be taller than " + MIN_CAPACITY);
        }
        return true;
    }

}
