package com.es.slots.public_holiday;


import com.es.slots.public_holiday.dto.requests.PublicHolidayRequestDTO;
import com.es.slots.public_holiday.dto.responses.PublicHolidayResponseDTO;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayAlreadyExistException;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayNotFoundException;
import com.es.slots.public_holiday.services.PublicHolidayService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("public-holiday")
@AllArgsConstructor
public class PublicHolidayController {

    private PublicHolidayService publicHolidayService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    /**
     * Create a new public holiday
     * @param publicHolidayRequestDto
     * @return PublicHolidayResponseDTO
     * @throws PublicHolidayAlreadyExistException
     */
    public ResponseEntity<PublicHolidayResponseDTO> create(@RequestBody @Valid PublicHolidayRequestDTO publicHolidayRequestDto) throws PublicHolidayAlreadyExistException {
        PublicHolidayResponseDTO publicHolidayResponseDto = publicHolidayService.create(publicHolidayRequestDto);
        return new ResponseEntity<>(publicHolidayResponseDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    /**
     * Get a public holiday by its publicId
     * @param publicId
     * @return
     * @throws PublicHolidayNotFoundException
     */
    public ResponseEntity<PublicHolidayResponseDTO> getByPublicId(@PathVariable("id") String publicId) throws PublicHolidayNotFoundException {
        PublicHolidayResponseDTO publicHolidayResponseDto = publicHolidayService.getPublicHolidayById(publicId);
        return new ResponseEntity<>(publicHolidayResponseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    /**
     * Get all public holidays
     * @return List<PublicHolidayResponseDTO>
     */
    public ResponseEntity<List<PublicHolidayResponseDTO>> get() {
        List<PublicHolidayResponseDTO> publicHolidayResponseDtos = publicHolidayService.getPublicHolidays();
        return new ResponseEntity<>(publicHolidayResponseDtos, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("{id}")

    /**
    * Update a public holiday
    * @param publicId
    * @param publicHolidayRequestDto
    * @return PublicHolidayResponseDTO
    * @throws PublicHolidayNotFoundException
    * @throws PublicHolidayAlreadyExistException
    */
    public ResponseEntity<PublicHolidayResponseDTO> update(@PathVariable("id") String publicId, @RequestBody @Valid PublicHolidayRequestDTO publicHolidayRequestDto) throws PublicHolidayNotFoundException, PublicHolidayAlreadyExistException {
        PublicHolidayResponseDTO publicHolidayResponseDto = publicHolidayService.update(publicId, publicHolidayRequestDto);
        return new ResponseEntity<>(publicHolidayResponseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")

    /**
    * Delete a public holiday
    * @param publicId
    * @return
    * @throws PublicHolidayNotFoundException
    */
    public ResponseEntity<PublicHolidayResponseDTO> delete(@PathVariable("id") String publicId) throws PublicHolidayNotFoundException {
        publicHolidayService.delete(publicId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
