package com.es.slots.util;

import com.es.slots.slot.dtos.requests.ExceptionalSlotRequestDTO;
import com.es.slots.slot.dtos.requests.SlotRequestDTO;
import com.es.slots.slot.dtos.requests.StandardSlotRequestDTO;
import com.es.slots.slot.dtos.responses.ExceptionalSlotResponseDTO;
import com.es.slots.slot.dtos.responses.SlotResponseDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.entities.Slot;
import com.es.slots.slot.entities.StandardSlot;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
 
public class SlotUtil {
 
 
 
    public static ExceptionalSlot createExceptionalSlot(ExceptionalSlotRequestDTO slotRequestDto) {
        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        exceptionalSlot.setStartHour(slotRequestDto.getStartHour());
        exceptionalSlot.setEndHour(slotRequestDto.getEndHour());
        exceptionalSlot.setCapacity(slotRequestDto.getCapacity());
        exceptionalSlot.setDayDate(slotRequestDto.getDate());
        return exceptionalSlot;
    }

    public static ExceptionalSlot createExceptionalSlot(LocalTime startHour, LocalTime endHour, int capacity, LocalDate day) {
        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        exceptionalSlot.setStartHour(startHour);
        exceptionalSlot.setEndHour(endHour);
        exceptionalSlot.setCapacity(capacity);
        exceptionalSlot.setDayDate(day);
        return exceptionalSlot;
    }
 
    
 
      

    public static SlotResponseDTO createExceptionalSlotResponseDTO(LocalTime startHour, LocalTime endHour, int capacity, LocalDate day) {
         ExceptionalSlotResponseDTO slotDto = new ExceptionalSlotResponseDTO();
         slotDto.setStartHour(startHour);
         slotDto.setEndHour(endHour);
         slotDto.setDate(day);
         slotDto.setCapacity(capacity);
         slotDto.setCurrentCharge(2);
        
    //     slotResponseDto.setOverlappingSlots(List.of());
         return slotDto;
     }

     public static ExceptionalSlotResponseDTO createExceptionalSlotResponseDTO(ExceptionalSlot slotDto) {
         ExceptionalSlotResponseDTO slotResponseDto = new ExceptionalSlotResponseDTO();
         slotResponseDto.setCapacity(slotDto.getCapacity());
        slotResponseDto.setCurrentCharge(2);
        slotResponseDto.setDate(slotDto.getDayDate());
        slotResponseDto.setStartHour(slotDto.getStartHour());
        slotResponseDto.setEndHour(slotDto.getEndHour());
         return slotResponseDto;
     }



    public static ExceptionalSlotRequestDTO createExceptionalSlotRequestDTO(LocalTime startHour, LocalTime endHour, int capacity, LocalDate day) {
        ExceptionalSlotRequestDTO exceptionalSlotRequestDTO = new ExceptionalSlotRequestDTO();
        exceptionalSlotRequestDTO.setStartHour(startHour);
        exceptionalSlotRequestDTO.setEndHour(endHour);
        exceptionalSlotRequestDTO.setCapacity(capacity);
        exceptionalSlotRequestDTO.setDate(day);
        return exceptionalSlotRequestDTO;
    }
    /**
     public static void assertSlotEquals(SlotDTO expected, Slot actual) {
         assert expected.getStartHour().equals(actual.getStartHour());
         assert expected.getEndHour().equals(actual.getEndHour());
         assert expected.getCapacity() == actual.getCapacity();
   //        Here instead of mocking the UUID that is static class, we can ensure that the UUID is not null and that it is a valid UUID.
         assert UUID.fromString(expected.getPublicId()).toString().equals(expected.getPublicId());
     }
      public static void assertExceptionalSlotEquals(SlotDTO expected, Slot actual) {
         assert expected.getStartHour().equals(actual.getStartHour());
         assert expected.getEndHour().equals(actual.getEndHour());
         assert expected.getCapacity() == actual.getCapacity();
   //        Here instead of mocking the UUID that is static class, we can ensure that the UUID is not null and that it is a valid UUID.
         assert UUID.fromString(expected.getPublicId()).toString().equals(expected.getPublicId());
     }*/
}