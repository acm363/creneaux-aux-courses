package com.es.slots.public_holiday.services;

import com.es.slots.public_holiday.dto.requests.PublicHolidayRequestDTO;
import com.es.slots.public_holiday.dto.responses.PublicHolidayResponseDTO;
import com.es.slots.public_holiday.entities.PublicHoliday;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayAlreadyExistException;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayNotFoundException;
import com.es.slots.public_holiday.mapper.PublicHolidayMapper;
import com.es.slots.public_holiday.repositories.PublicHolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PublicHolidayService {

    @Autowired
    private PublicHolidayRepository publicHolidayRepository;

    @Autowired
    private PublicHolidayMapper publicHolidayMapper;


    /**
     * Create a new public holiday
     *
     * @param publicHolidayRequestDto The public holiday body to create
     * @return The created public holiday
     * @throws PublicHolidayAlreadyExistException If encounter a non-valid rule on the public holiday creation.
     */
    public PublicHolidayResponseDTO create(PublicHolidayRequestDTO publicHolidayRequestDto) throws PublicHolidayAlreadyExistException {
        PublicHoliday publicHoliday = createNewPublicHoliday(publicHolidayRequestDto);

        publicHolidayRepository.save(publicHoliday);

        return publicHolidayMapper.buildPublicHolidayResponseDto(publicHoliday);
    }

    /**
     * Create a new public holiday
     * @param publicHolidayRequestDto
     * @return PublicHoliday
     * @throws PublicHolidayAlreadyExistException
     */
    private PublicHoliday createNewPublicHoliday(PublicHolidayRequestDTO publicHolidayRequestDto) throws PublicHolidayAlreadyExistException {

        Optional<PublicHoliday> publicHolidayWithSameDate = publicHolidayRepository.findByDate(publicHolidayRequestDto.getDate()).stream().findFirst();

        if (publicHolidayWithSameDate.isPresent()) {
            throw new PublicHolidayAlreadyExistException();
        }

        PublicHoliday publicHoliday = new PublicHoliday();

        String publicId = UUID.randomUUID().toString();

        publicHoliday.setPublicId(publicId);

        publicHoliday.setDate(publicHolidayRequestDto.getDate());
        publicHoliday.setLabel(publicHolidayRequestDto.getLabel());

        return publicHoliday;
    }


    /**
     * Get a public holiday by its public id
     *
     * @param publicId The public id of the public holiday to get
     * @return The public holiday
     * @throws PublicHolidayNotFoundException If the public holiday is not found
     */
    public PublicHolidayResponseDTO getPublicHolidayById(String publicId) throws PublicHolidayNotFoundException {
        PublicHoliday publicHoliday = publicHolidayRepository.findByPublicId(publicId).orElseThrow(() -> new PublicHolidayNotFoundException(publicId));
        return getPublicHolidayResponseDto(publicHoliday);
    }

    private PublicHolidayResponseDTO getPublicHolidayResponseDto(PublicHoliday publicHoliday) {
        return publicHolidayMapper.buildPublicHolidayResponseDto(publicHoliday);
    }

    /**
     * Get all the public holidays
     *
     * @return The list of all public holidays
     */
    public List<PublicHolidayResponseDTO> getPublicHolidays() {
        List<PublicHoliday> publicHolidays = publicHolidayRepository.findAll();
        List<PublicHolidayResponseDTO> publicHolidayResponseDtos = new ArrayList<>();

        for (PublicHoliday publicHoliday : publicHolidays) {
            publicHolidayResponseDtos.add(getPublicHolidayResponseDto(publicHoliday));
        }

        return publicHolidayResponseDtos;
    }


    /**
     * Delete a public holiday
     *
     * @param publicId The public id of the public holiday to delete
     * @throws PublicHolidayNotFoundException If the public holiday is not found
     */
    public void delete(String publicId) throws PublicHolidayNotFoundException {
        PublicHoliday publicHoliday = publicHolidayRepository.findByPublicId(publicId).orElseThrow(() -> new PublicHolidayNotFoundException(publicId));
        publicHolidayRepository.delete(publicHoliday);
    }

    /**
     * Update a public holiday
     * For now, the update will always update all the fields of the public holiday.
     *
     * @param publicHolidayRequestDto The public holiday body to update
     * @return The updated public holiday
     * @throws PublicHolidayNotFoundException     If the public holiday is not found
     * @throws PublicHolidayAlreadyExistException If encounter a non-valid rule on the public holiday update.
     */
    public PublicHolidayResponseDTO update(String publicId, PublicHolidayRequestDTO publicHolidayRequestDto) throws PublicHolidayNotFoundException, PublicHolidayAlreadyExistException {
        PublicHoliday publicHoliday = publicHolidayRepository.findByPublicId(publicId).orElseThrow(() -> new PublicHolidayNotFoundException(publicId));

        Optional<PublicHoliday> publicHolidayWithSameDate = publicHolidayRepository.findByDate(publicHolidayRequestDto.getDate()).stream().filter(p -> !p.getPublicId().equals(publicId)).findFirst();

        if (publicHolidayWithSameDate.isPresent()) {
            throw new PublicHolidayAlreadyExistException();
        }

        publicHoliday.setDate(publicHolidayRequestDto.getDate());
        publicHoliday.setLabel(publicHolidayRequestDto.getLabel());

        publicHolidayRepository.save(publicHoliday);
        return publicHolidayMapper.buildPublicHolidayResponseDto(publicHoliday);
    }

    /**
     * Check if a date is a public holiday
     *
     * @param date The date to check
     * @return True if the date is a public holiday, false otherwise
     */
    public boolean isPublicHoliday(LocalDate date) {
        List<PublicHoliday> publicHolidays = publicHolidayRepository.findByDate(date);
        return !publicHolidays.isEmpty();
    }
}
