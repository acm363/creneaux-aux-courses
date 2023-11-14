package com.es.slots.slot.validators;

import com.es.slots.slot.dtos.requests.SlotRequestDTO;
import com.es.slots.slot.dtos.requests.StandardSlotRequestDTO;
import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SlotValidatorTest {

    @Autowired
    private SlotValidator slotValidator;

    @MockBean
    private SlotRequestDTO slotRequestDTO;

    @Test
    public void testCheckValidityOfSlotRequestValid() throws SlotValidityFailureException {
        when(slotRequestDTO.getStartHour()).thenReturn(LocalTime.of(10, 0));
        when(slotRequestDTO.getEndHour()).thenReturn(LocalTime.of(12, 0));
        when(slotRequestDTO.getCapacity()).thenReturn(5);

        assertTrue(slotValidator.checkValidityOfSlotRequest(slotRequestDTO));
    }

    @Test
    public void testCheckValidityOfSlotRequestInvalidHours() {
        when(slotRequestDTO.getStartHour()).thenReturn(LocalTime.of(14, 0));
        when(slotRequestDTO.getEndHour()).thenReturn(LocalTime.of(12, 0));

        Assertions.assertThrows(SlotValidityFailureException.class, () -> {
            slotValidator.checkValidityOfSlotRequest(slotRequestDTO);
        });
    }

    @Test
    public void testCheckValidityOfSlotRequestInvalidStartHour() {
        when(slotRequestDTO.getStartHour()).thenReturn(LocalTime.of(7, 0));
        when(slotRequestDTO.getEndHour()).thenReturn(LocalTime.of(12, 0));

        Assertions.assertThrows(SlotValidityFailureException.class, () -> {
            slotValidator.checkValidityOfSlotRequest(slotRequestDTO);
        });
    }

    @Test
    public void testCheckValidityOfSlotRequestInvalidEndHour() {
        when(slotRequestDTO.getStartHour()).thenReturn(LocalTime.of(10, 0));
        when(slotRequestDTO.getEndHour()).thenReturn(LocalTime.of(21, 0));

        Assertions.assertThrows(SlotValidityFailureException.class, () -> {
            slotValidator.checkValidityOfSlotRequest(slotRequestDTO);
        });
    }

    @Test
    public void testCheckValidityOfSlotRequestInvalidCapacity() {
        when(slotRequestDTO.getStartHour()).thenReturn(LocalTime.of(10, 0));
        when(slotRequestDTO.getEndHour()).thenReturn(LocalTime.of(12, 0));
        when(slotRequestDTO.getCapacity()).thenReturn(0);

        Assertions.assertThrows(SlotValidityFailureException.class, () -> {
            slotValidator.checkValidityOfSlotRequest(slotRequestDTO);
        });
    }
}