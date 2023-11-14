package com.es.slots.slot.StandardSlot;
//package com.es.slots.slot;
//
//import com.es.slots.public_holiday.services.PublicHolidayService;
//import com.es.slots.slot.dtos.requests.SlotRequestDTO;
//import com.es.slots.slot.dtos.responses.SlotResponseDTO;
//import com.es.slots.slot.entities.Slot;
//import com.es.slots.slot.entities.StandardSlot;
//import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
//import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
//import com.es.slots.slot.repositories.SlotRepository;
//import com.es.slots.slot.services.SlotService;
//import com.es.slots.util.SlotUtil;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class SlotServiceUnitTest {
//
//    @Autowired
//    SlotService slotService;
//
//    @MockBean
//    SlotRepository slotRepository;
//
//    @MockBean
//    PublicHolidayService publicHolidayService;
//
//    @Test
//    void should_create_standard_slot_respecting_all_conditions() throws SlotValidityFailureException {
//        // Given.
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        StandardSlot standardSlot = SlotUtil.createStandardSlot(slotRequestDto);
//        when(slotRepository.save(any(Slot.class))).thenReturn(standardSlot);
//
//        // When.
//        SlotResponseDTO slotResponseDTO = slotService.create(slotRequestDto);
//
//        // Then.
//        assert slotResponseDTO.getOverlappingSlots().isEmpty();
//        SlotUtil.assertSlotEquals(slotResponseDTO.getSlotDto(), standardSlot);
//    }
//
//    @Test
//    void should_throw_exception_when_start_hour_is_after_end_hour() {
//        // Given.
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(11, 0), LocalTime.of(9, 0), LocalDate.now(), 10, SlotType.STANDARD);
//
//        // When.
//        // Then.
//        Assertions.assertThrows(SlotValidityFailureException.class, () -> slotService.create(slotRequestDto));
//
//        verify(slotRepository, never()).save(any(Slot.class));
//    }
//
//    @Test
//    void should_throw_exception_when_start_hour_is_before_8am() {
//        // Given.
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(7, 59), LocalTime.of(9, 0), LocalDate.now(), 10, SlotType.STANDARD);
//
//        // When.
//        // Then.
//        Assertions.assertThrows(SlotValidityFailureException.class, () -> slotService.create(slotRequestDto));
//
//        verify(slotRepository, never()).save(any(Slot.class));
//    }
//
//    @Test
//    void should_throw_exception_when_end_hour_is_after_8pm() {
//        // Given.
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(19, 0), LocalTime.of(20, 1), LocalDate.now(), 10, SlotType.STANDARD);
//
//        // When.
//        // Then.
//        Assertions.assertThrows(SlotValidityFailureException.class, () -> slotService.create(slotRequestDto));
//
//        verify(slotRepository, never()).save(any(Slot.class));
//    }
//
//    @Test
//    void should_throw_exception_when_capacity_is_less_than_1() {
//        // Given.
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 0, SlotType.STANDARD);
//
//        // When.
//        // Then.
//        Assertions.assertThrows(SlotValidityFailureException.class, () -> slotService.create(slotRequestDto));
//
//        verify(slotRepository, never()).save(any(Slot.class));
//    }
//
//
//    @Test
//    void should_return_overlapping_slots_when_there_are_overlapping_slots() throws SlotValidityFailureException {
//        // Given.
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(9, 0), LocalTime.of(12, 0), LocalDate.now(), 10, SlotType.STANDARD);
//
//        // Mock behavior for the slotRepository to return overlapping slots
//        StandardSlot standardSlot = SlotUtil.createStandardSlot(slotRequestDto);
//
//        Slot overlappingSlot1 = SlotUtil.createStandardSlot(LocalTime.of(10, 0), LocalTime.of(12, 0), slotRequestDto.getDay(), 5);
//        overlappingSlot1.setPublicId(UUID.randomUUID().toString());
//
//        Slot overlappingSlot2 = SlotUtil.createStandardSlot(LocalTime.of(11, 0), LocalTime.of(13, 0), slotRequestDto.getDay(), 7);
//        overlappingSlot2.setPublicId(UUID.randomUUID().toString());
//
//        when(slotRepository.findByDayDate(slotRequestDto.getDay())).thenReturn(new ArrayList<>(List.of(overlappingSlot1, overlappingSlot2)));
//        when(slotRepository.save(any(Slot.class))).thenReturn(standardSlot);
//
//
//        // When.
//        SlotResponseDTO slotResponseDTO = slotService.create(slotRequestDto);
//
//        // Then.
//        assert slotResponseDTO.getOverlappingSlots().size() == 2;
//
//        SlotUtil.assertSlotEquals(slotResponseDTO.getOverlappingSlots().get(0), overlappingSlot1);
//        SlotUtil.assertSlotEquals(slotResponseDTO.getOverlappingSlots().get(1), overlappingSlot2);
//        SlotUtil.assertSlotEquals(slotResponseDTO.getSlotDto(), standardSlot);
//
//        verify(slotRepository, times(1)).save(any(Slot.class));
//        verify(slotRepository, times(1)).findByDayDate(slotRequestDto.getDay());
//    }
//
//    @Test
//    void should_get_a_slot_by_id() throws SlotNotFoundException {
//        // Given.
//        Slot slot = SlotUtil.createStandardSlot(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 15);
//        String slotPublicId = UUID.randomUUID().toString();
//        slot.setPublicId(slotPublicId);
//        when(slotRepository.findByPublicId(slotPublicId)).thenReturn(Optional.of(slot));
//
//        // When.
//        SlotResponseDTO slotResponseDTO = slotService.getSlotResponseDtoByPublicId(slot.getPublicId());
//
//        // Then.
//        SlotUtil.assertSlotEquals(slotResponseDTO.getSlotDto(), slot);
//    }
//
//    @Test
//    void should_throw_exception_when_slot_not_found() {
//        // Given.
//        String slotPublicId = UUID.randomUUID().toString();
//        when(slotRepository.findByPublicId(slotPublicId)).thenReturn(Optional.empty());
//
//        // When.
//        // Then.
//        Assertions.assertThrows(SlotNotFoundException.class, () -> slotService.getSlotResponseDtoByPublicId(slotPublicId));
//    }
//
//    @Test
//    void should_get_all_slots() {
//        // Given.
//        Slot slot1 = SlotUtil.createStandardSlot(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 15);
//        String slot1PublicId = UUID.randomUUID().toString();
//        slot1.setPublicId(slot1PublicId);
//
//        Slot slot2 = SlotUtil.createStandardSlot(LocalTime.of(11, 0), LocalTime.of(13, 0), LocalDate.now(), 20);
//        String slot2PublicId = UUID.randomUUID().toString();
//        slot2.setPublicId(slot2PublicId);
//
//        when(slotRepository.findAll()).thenReturn(new ArrayList<>(List.of(slot1, slot2)));
//
//        // When.
//        List<SlotResponseDTO> slotResponseDtos = slotService.getSlots();
//
//        // Then.
//        assert slotResponseDtos.size() == 2;
//
//        SlotUtil.assertSlotEquals(slotResponseDtos.get(0).getSlotDto(), slot1);
//        SlotUtil.assertSlotEquals(slotResponseDtos.get(1).getSlotDto(), slot2);
//    }
//
//
//    @Test
//    void should_update_slot() throws SlotNotFoundException, SlotValidityFailureException {
//        // Given.
//        String publicId = UUID.randomUUID().toString();
//        StandardSlot standardSlot = SlotUtil.createStandardSlot(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10);
//        standardSlot.setPublicId(publicId);
//        when(slotRepository.findByPublicId(publicId)).thenReturn(Optional.of(standardSlot));
//        when(slotRepository.save(any(Slot.class))).thenReturn(standardSlot);
//        SlotRequestDTO slotRequestDto_update = SlotUtil.createSlotRequestDTO(LocalTime.of(10, 0), LocalTime.of(11, 0), LocalDate.now(), 20, SlotType.STANDARD);
//
//
//        // When.
//        SlotResponseDTO slotResponseDTO = slotService.update(publicId, slotRequestDto_update);
//        // Then.
//        Assertions.assertEquals(slotResponseDTO.getSlotDto().getStartHour(), standardSlot.getStartHour());
//    }
//
//
//    @Test
//    void should_throw_exception_when_slot_not_found_for_update() {
//        // Given.
//        String publicId = UUID.randomUUID().toString();
//        when(slotRepository.findByPublicId(publicId)).thenReturn(Optional.empty());
//        SlotRequestDTO slotRequestDTO = SlotUtil.createSlotRequestDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 5, SlotType.STANDARD);
//
//
//        // When.
//        // Then.
//        Assertions.assertThrows(SlotNotFoundException.class, () -> slotService.update(publicId, slotRequestDTO));
//    }
//
//    // create me a test that will test the update method when the startHour is before 8am
//    @Test
//    void should_throw_exception_when_start_hour_is_before_8am_for_update() {
//        // Given.
//        String publicId = UUID.randomUUID().toString();
//        StandardSlot standardSlot = SlotUtil.createStandardSlot(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10);
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(7, 59), LocalTime.of(9, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        standardSlot.setPublicId(publicId);
//        when(slotRepository.findByPublicId(publicId)).thenReturn(Optional.of(standardSlot));
//
//        // Then.
//        Assertions.assertThrows(SlotValidityFailureException.class, () -> slotService.update(publicId, slotRequestDto));
//
//        verify(slotRepository, never()).save(any(Slot.class));
//    }
//
//    @Test
//    void should_throw_exception_when_end_hour_is_after_8pm_for_update() throws SlotNotFoundException, SlotValidityFailureException {
//        // Given.
//        String publicId = UUID.randomUUID().toString();
//        StandardSlot standardSlot = SlotUtil.createStandardSlot(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10);
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(9, 0), LocalTime.of(23, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        standardSlot.setPublicId(publicId);
//        when(slotRepository.findByPublicId(publicId)).thenReturn(Optional.of(standardSlot));
//
//        // Then.
//        Assertions.assertThrows(SlotValidityFailureException.class, () -> slotService.update(publicId, slotRequestDto));
//
//        verify(slotRepository, never()).save(any(Slot.class));
//    }
//
//    @Test
//    void should_throw_exception_when_capacity_is_less_than_1_for_update() {
//        // Given.
//        String publicId = UUID.randomUUID().toString();
//        StandardSlot standardSlot = SlotUtil.createStandardSlot(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10);
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 0, SlotType.STANDARD);
//        standardSlot.setPublicId(publicId);
//        when(slotRepository.findByPublicId(publicId)).thenReturn(Optional.of(standardSlot));
//
//        // Then.
//        Assertions.assertThrows(SlotValidityFailureException.class, () -> slotService.update(publicId, slotRequestDto));
//
//        verify(slotRepository, never()).save(any(Slot.class));
//    }
//
//    @Test
//    void should_delete_slot() throws SlotNotFoundException {
//        // Given.
//        String publicId = UUID.randomUUID().toString();
//        StandardSlot standardSlot = SlotUtil.createStandardSlot(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10);
//        standardSlot.setPublicId(publicId);
//        when(slotRepository.findByPublicId(publicId)).thenReturn(Optional.of(standardSlot));
//
//        // When.
//        slotService.delete(publicId);
//
//        // Then.
//        verify(slotRepository, times(1)).delete(standardSlot);
//    }
//
//    @Test
//    void should_throw_exception_when_slot_not_found_for_delete() {
//        // Given.
//        String publicId = UUID.randomUUID().toString();
//        when(slotRepository.findByPublicId(publicId)).thenReturn(Optional.empty());
//
//        // When.
//        // Then.
//        Assertions.assertThrows(SlotNotFoundException.class, () -> slotService.delete(publicId));
//    }
//
//
//}
