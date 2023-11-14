package com.es.slots.util;

import com.es.slots.public_holiday.dto.requests.PublicHolidayRequestDTO;
import com.es.slots.public_holiday.dto.responses.PublicHolidayResponseDTO;
import com.es.slots.public_holiday.entities.PublicHoliday;

import java.time.LocalDate;
import java.util.UUID;

public class PublicHolidayUtil {

    // Helper method to create a PublicHolidayRequestDTO object
    public static PublicHolidayRequestDTO createPublicHolidayRequestDTO(LocalDate date, String label) {
        PublicHolidayRequestDTO dto = new PublicHolidayRequestDTO();
        dto.setDate(date);
        dto.setLabel(label);
        return dto;
    }

    // Helper method to create a PublicHolidayResponseDTO object
    public static PublicHolidayResponseDTO createPublicHolidayResponseDTO(LocalDate date, String label) {
        PublicHolidayResponseDTO dto = new PublicHolidayResponseDTO();
        dto.setPublicId(UUID.randomUUID().toString());
        dto.setDate(date);
        dto.setLabel(label);
        return dto;
    }

    // Helper method to create a PublicHoliday entity
    public static PublicHoliday createPublicHoliday(LocalDate date, String label) {
        PublicHoliday holiday = new PublicHoliday();
        holiday.setDate(date);
        holiday.setPublicId(UUID.randomUUID().toString());
        holiday.setLabel(label);
        return holiday;
    }

    public static void assertPublicHolidayEquals(PublicHolidayResponseDTO expected,  PublicHolidayResponseDTO actual) {
        assert actual.getDate().equals(expected.getDate());
        assert actual.getLabel().equals(expected.getLabel());
        assert UUID.fromString(expected.getPublicId()).toString().equals(expected.getPublicId());
    }
}
