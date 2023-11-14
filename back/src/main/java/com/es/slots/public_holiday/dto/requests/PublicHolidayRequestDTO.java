package com.es.slots.public_holiday.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
// Represents a public holiday request DTO.
public class PublicHolidayRequestDTO {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Label is required")
    private String label;

}
