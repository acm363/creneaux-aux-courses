package com.es.slots.public_holiday.mapper;

import com.es.slots.public_holiday.dto.responses.PublicHolidayResponseDTO;
import com.es.slots.public_holiday.entities.PublicHoliday;
import org.springframework.stereotype.Component;

@Component
public class PublicHolidayMapper {

    /**
     * Builds a public holiday response DTO
     * @param publicHoliday
     * @return PublicHolidayResponseDTO
     */
    public PublicHolidayResponseDTO buildPublicHolidayResponseDto(PublicHoliday publicHoliday) {
        PublicHolidayResponseDTO publicHolidayResponseDto = new PublicHolidayResponseDTO();
        publicHolidayResponseDto.setDate(publicHoliday.getDate());
        publicHolidayResponseDto.setLabel(publicHoliday.getLabel());
        publicHolidayResponseDto.setPublicId(publicHoliday.getPublicId());
        return publicHolidayResponseDto;
    }

}
