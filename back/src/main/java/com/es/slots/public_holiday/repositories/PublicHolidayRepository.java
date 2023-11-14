package com.es.slots.public_holiday.repositories;

import com.es.slots.public_holiday.entities.PublicHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Represents a public holiday repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {
    List<PublicHoliday> findByDate(LocalDate date);

    Optional<PublicHoliday> findByDateAndLabel(LocalDate date, String label);

    Optional<PublicHoliday> findByPublicId(String publicId);
}
