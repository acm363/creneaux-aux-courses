package com.es.slots.slot.services.dtos;

import com.es.slots.public_holiday.services.PublicHolidayService;
import com.es.slots.slot.dtos.responses.ExceptionalSlotResponseDTO;
import com.es.slots.slot.dtos.responses.OverlappingSlotResponseDTO;
import com.es.slots.slot.dtos.responses.StandardSlotResponseDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.entities.StandardSlot;
import com.es.slots.slot.mapper.ExceptionalSlotMapper;
import com.es.slots.slot.mapper.StandardSlotMapper;
import com.es.slots.slot.services.ExceptionalSlotService;
import com.es.slots.slot.services.StandardSlotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OverlappingSlotDtoServiceTest {

    @Autowired
    private OverlappingSlotDtoService overlappingSlotDtoService;

    @MockBean
    private StandardSlotService standardSlotService;

    @MockBean
    private StandardSlotMapper standardSlotMapper;

    @MockBean
    private ExceptionalSlotService exceptionalSlotService;

    @MockBean
    private ExceptionalSlotMapper exceptionalSlotMapper;

    @MockBean
    private PublicHolidayService publicHolidayService;



    @Test
    void getOverlappingSlotResponseDTO_withStandardSlot_WithoutHoliday() {
        StandardSlot standardSlot = new StandardSlot();
        standardSlot.setStartHour(LocalTime.of(11, 0));
        standardSlot.setEndHour(LocalTime.of(13, 0));
        standardSlot.setDayOfWeek(LocalDate.now().getDayOfWeek());


        StandardSlotResponseDTO standardSlotResponseDTO = new StandardSlotResponseDTO();
        standardSlotResponseDTO.setStartHour(standardSlot.getStartHour());
        Mockito.when(standardSlotMapper.buildResponseFromStandardSlot(standardSlot)).thenReturn(standardSlotResponseDTO);


        List<ExceptionalSlot> exceptionalSlots = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
            exceptionalSlot.setStartHour(LocalTime.of(10 + i, 0));
            exceptionalSlot.setEndHour(LocalTime.of(12 + i, 0));
            exceptionalSlot.setDayDate(LocalDate.now());
            exceptionalSlots.add(exceptionalSlot);
        }
        Mockito.when(exceptionalSlotService.getAllByDate(Mockito.any())).thenReturn(exceptionalSlots);

        List<StandardSlot> standardSlots = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            StandardSlot standardSlot_ = new StandardSlot();
            standardSlot_.setStartHour(LocalTime.of(10 + i, 0));
            standardSlot_.setEndHour(LocalTime.of(12 + i, 0));
            standardSlot_.setDayOfWeek(LocalDate.now().getDayOfWeek());
            standardSlots.add(standardSlot_);
        }
        Mockito.when(standardSlotService.getAllByDay(Mockito.any())).thenReturn(standardSlots);


        Mockito.when(publicHolidayService.isPublicHoliday(Mockito.any())).thenReturn(false);

        OverlappingSlotResponseDTO overlappingSlotResponseDTO = overlappingSlotDtoService.getOverlappingSlotResponseDTO(standardSlot);

        Assertions.assertNotNull(overlappingSlotResponseDTO);
        Assertions.assertEquals(standardSlotResponseDTO, overlappingSlotResponseDTO.getSlotDto());
        Assertions.assertEquals(6, overlappingSlotResponseDTO.getOverlappingSlots().size());

        Mockito.verify(standardSlotMapper, Mockito.times(1)).buildResponseFromStandardSlot(standardSlot);
        Mockito.verify(exceptionalSlotMapper, Mockito.times(3)).buildResponseFromExceptionalSlot(Mockito.any());
        Mockito.verify(exceptionalSlotService, Mockito.times(1)).getAllByDate(Mockito.any());
        Mockito.verify(standardSlotService, Mockito.times(1)).getAllByDay(Mockito.any());
        Mockito.verify(publicHolidayService, Mockito.times(1)).isPublicHoliday(Mockito.any());

    }


    @Test
    void getOverlappingSlotResponseDTO_withStandardSlot_WithHoliday() {
        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        exceptionalSlot.setStartHour(LocalTime.of(11, 0));
        exceptionalSlot.setEndHour(LocalTime.of(13, 0));
        exceptionalSlot.setDayDate(LocalDate.now());


        ExceptionalSlotResponseDTO exceptionalSlotResponseDTO = new ExceptionalSlotResponseDTO();
        exceptionalSlotResponseDTO.setStartHour(exceptionalSlot.getStartHour());
        Mockito.when(exceptionalSlotMapper.buildResponseFromExceptionalSlot(exceptionalSlot)).thenReturn(exceptionalSlotResponseDTO);


        List<ExceptionalSlot> exceptionalSlots = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ExceptionalSlot slot = new ExceptionalSlot();
            slot.setStartHour(LocalTime.of(10 + i, 0));
            slot.setEndHour(LocalTime.of(12 + i, 0));
            slot.setDayDate(LocalDate.now());
            exceptionalSlots.add(slot);
        }
        Mockito.when(exceptionalSlotService.getAllByDate(Mockito.any())).thenReturn(exceptionalSlots);

        List<StandardSlot> standardSlots = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            StandardSlot standardSlot_ = new StandardSlot();
            standardSlot_.setStartHour(LocalTime.of(10 + i, 0));
            standardSlot_.setEndHour(LocalTime.of(12 + i, 0));
            standardSlot_.setDayOfWeek(LocalDate.now().getDayOfWeek());
            standardSlots.add(standardSlot_);
        }
        Mockito.when(standardSlotService.getAllByDay(Mockito.any())).thenReturn(standardSlots);


        Mockito.when(publicHolidayService.isPublicHoliday(Mockito.any())).thenReturn(true);

        OverlappingSlotResponseDTO overlappingSlotResponseDTO = overlappingSlotDtoService.getOverlappingSlotResponseDTO(exceptionalSlot);

        Assertions.assertNotNull(overlappingSlotResponseDTO);
        Assertions.assertEquals(exceptionalSlotResponseDTO, overlappingSlotResponseDTO.getSlotDto());
        Assertions.assertEquals(3, overlappingSlotResponseDTO.getOverlappingSlots().size());

        Mockito.verify(standardSlotMapper, Mockito.times(0)).buildResponseFromStandardSlot(Mockito.any());
        Mockito.verify(exceptionalSlotMapper, Mockito.times(1)).buildResponseFromExceptionalSlot(exceptionalSlot);
        Mockito.verify(exceptionalSlotService, Mockito.times(1)).getAllByDate(Mockito.any());
        Mockito.verify(standardSlotService, Mockito.times(0)).getAllByDay(Mockito.any());
        Mockito.verify(publicHolidayService, Mockito.times(1)).isPublicHoliday(Mockito.any());

    }
}