package com.es.slots.public_holiday;

import com.es.slots.public_holiday.dto.requests.PublicHolidayRequestDTO;
import com.es.slots.public_holiday.dto.responses.PublicHolidayResponseDTO;
import com.es.slots.public_holiday.entities.PublicHoliday;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayNotFoundException;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayAlreadyExistException;
import com.es.slots.public_holiday.repositories.PublicHolidayRepository;
import com.es.slots.public_holiday.services.PublicHolidayService;
import com.es.slots.util.PublicHolidayUtil;
import jakarta.validation.constraints.AssertTrue;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class PublicHolidayServiceUnitTest {

    @Autowired
    PublicHolidayService publicHolidayService;

    @MockBean
    PublicHolidayRepository publicHolidayRepository;


    @Test
    void should_create_public_holiday() throws PublicHolidayAlreadyExistException {
        // Given.
        LocalDate date = LocalDate.now().plusDays(10);
        String label = "Sample Holiday";
        PublicHoliday publicHolidayToCreate = PublicHolidayUtil.createPublicHoliday(date, label);
        PublicHolidayRequestDTO creationDto = PublicHolidayUtil.createPublicHolidayRequestDTO(date, label);
        when(publicHolidayRepository.save(any(PublicHoliday.class))).thenReturn(publicHolidayToCreate);

        // When.
        PublicHolidayResponseDTO result = publicHolidayService.create(creationDto);

        // Then.
        PublicHolidayResponseDTO expectedResponseDto = PublicHolidayUtil.createPublicHolidayResponseDTO(date, label);
        verify(publicHolidayRepository, times(1)).save(any(PublicHoliday.class));
        PublicHolidayUtil.assertPublicHolidayEquals(expectedResponseDto, result);
    }

    @Test
    void should_throw_exception_when_a_public_holiday_already_exist_for_this_date() {
        // Given.
        LocalDate date = LocalDate.now().plusDays(10);
        String label = "Sample Holiday";
        PublicHolidayRequestDTO publicHolidayRequestDto = PublicHolidayUtil.createPublicHolidayRequestDTO(date, label);

        PublicHoliday publicHoliday = PublicHolidayUtil.createPublicHoliday(date, label);
        when(publicHolidayRepository.findByDate(date)).thenReturn(List.of(publicHoliday));

        // When & Then.
        Assertions.assertThrows(PublicHolidayAlreadyExistException.class, () -> publicHolidayService.create(publicHolidayRequestDto));
    }

    @Test
    void should_get_all_public_holidays() {
        // Given.
        LocalDate date1 = LocalDate.now().plusDays(10);
        LocalDate date2 = LocalDate.now();
        String label1 = "Sample Holiday 1";
        String label2 = "Sample Holiday 2";
        PublicHoliday publicHoliday1 = PublicHolidayUtil.createPublicHoliday(date1, label1);
        PublicHoliday publicHoliday2 = PublicHolidayUtil.createPublicHoliday(date2, label2);
        when(publicHolidayRepository.findAll()).thenReturn(List.of(publicHoliday1, publicHoliday2));

        // When.
        List<PublicHolidayResponseDTO> result = publicHolidayService.getPublicHolidays();

        // Then.
        PublicHolidayResponseDTO expectedResponseDto1 = PublicHolidayUtil.createPublicHolidayResponseDTO(date1, label1);
        PublicHolidayResponseDTO expectedResponseDto2 = PublicHolidayUtil.createPublicHolidayResponseDTO(date2, label2);
        verify(publicHolidayRepository, times(1)).findAll();
        Assertions.assertEquals(2, result.size());
        PublicHolidayUtil.assertPublicHolidayEquals(expectedResponseDto1, result.get(0));
        PublicHolidayUtil.assertPublicHolidayEquals(expectedResponseDto2, result.get(1));
    }


    @Test
    void should_throw_exception_when_public_holiday_not_found() {
        // Given.
        String publicId = "4338cf58-ce05-4c59-9859-e46256048c44";
        when(publicHolidayRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        // When & Then.
        Assertions.assertThrows(PublicHolidayNotFoundException.class, () -> publicHolidayService.getPublicHolidayById(publicId));
    }

    @Test
    void should_check_if_date_is_a_public_holiday() {
        // Given.
        LocalDate date = LocalDate.now();
        PublicHoliday holiday = PublicHolidayUtil.createPublicHoliday(date, "Holiday Today");
        when(publicHolidayRepository.findByDate(date)).thenReturn(List.of(holiday));

        // When.
        boolean isPublicHoliday = publicHolidayService.isPublicHoliday(date);

        // Then.
        Assertions.assertTrue(isPublicHoliday);
    }


    @Test
    void should_update_public_holiday() throws PublicHolidayAlreadyExistException, PublicHolidayNotFoundException {
        // Given.
        LocalDate date = LocalDate.now();
        String label = "Sample Holiday";
        PublicHoliday publicHoliday = PublicHolidayUtil.createPublicHoliday(date, label);
        PublicHolidayRequestDTO updateDto = PublicHolidayUtil.createPublicHolidayRequestDTO(date, label);
        when(publicHolidayRepository.findByPublicId(publicHoliday.getPublicId())).thenReturn(Optional.of(publicHoliday));
        when(publicHolidayRepository.findByDate(date)).thenReturn(List.of(publicHoliday));
        when(publicHolidayRepository.save(any(PublicHoliday.class))).thenReturn(publicHoliday);

        // When.
        PublicHolidayResponseDTO result = publicHolidayService.update(publicHoliday.getPublicId(), updateDto);

        // Then.
        PublicHolidayResponseDTO expectedResponseDto = PublicHolidayUtil.createPublicHolidayResponseDTO(date, label);
        verify(publicHolidayRepository, times(1)).save(any(PublicHoliday.class));
        PublicHolidayUtil.assertPublicHolidayEquals(expectedResponseDto, result);
    }

    @Test
    void should_throw_exception_when_updating_non_existing_public_holiday() {
        // Given.
        LocalDate date = LocalDate.now();
        String label = "Sample Holiday";
        PublicHolidayRequestDTO updateDto = PublicHolidayUtil.createPublicHolidayRequestDTO(date, label);
        when(publicHolidayRepository.findByPublicId(anyString())).thenReturn(Optional.empty());

        // When & Then.
        Assertions.assertThrows(PublicHolidayNotFoundException.class, () -> publicHolidayService.update("4338cf58-ce05-4c59-9859-e46256048c44", updateDto));
    }

    @Test
    void should_throw_exception_when_the_updated_holiday_already_exist() {
        // Given.
        LocalDate date = LocalDate.now();
        String label = "Sample Holiday";
        PublicHoliday publicHoliday = PublicHolidayUtil.createPublicHoliday(date, label);
        PublicHoliday anotherPublicHoliday = PublicHolidayUtil.createPublicHoliday(date.plusDays(15), label);
        PublicHolidayRequestDTO updateDto = PublicHolidayUtil.createPublicHolidayRequestDTO(date.plusDays(15), label);
        when(publicHolidayRepository.findByPublicId(publicHoliday.getPublicId())).thenReturn(Optional.of(publicHoliday));
        when(publicHolidayRepository.findByDate(date.plusDays(15))).thenReturn(List.of(anotherPublicHoliday));

        // When & Then.
        Assertions.assertThrows(PublicHolidayAlreadyExistException.class, () -> publicHolidayService.update(publicHoliday.getPublicId(), updateDto));
    }

    @Test
    void should_remove_public_holiday() throws PublicHolidayNotFoundException {
        // Given.
        LocalDate date = LocalDate.now();
        String label = "Sample Holiday";
        PublicHoliday publicHoliday = PublicHolidayUtil.createPublicHoliday(date, label);
        when(publicHolidayRepository.findByPublicId(publicHoliday.getPublicId())).thenReturn(Optional.of(publicHoliday));

        // When.
        publicHolidayService.delete(publicHoliday.getPublicId());

        // Then.
        verify(publicHolidayRepository, times(1)).delete(publicHoliday);
    }

    @Test
    void should_throw_exception_when_deleting_non_existing_public_holiday() {
        // Given.
        when(publicHolidayRepository.findByPublicId(anyString())).thenReturn(Optional.empty());

        // When & Then.
        Assertions.assertThrows(PublicHolidayNotFoundException.class, () -> publicHolidayService.delete("4338cf58-ce05-4c59-9859-e46256048c44"));
    }
}
