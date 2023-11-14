package com.es.slots.slot.repositories;

import com.es.slots.slot.entities.ExceptionalSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExceptionalSlotRepository extends JpaRepository<ExceptionalSlot, Long> {
    Optional<ExceptionalSlot> findByPublicId(String publicId);

    List<ExceptionalSlot> findAllByDayDate(LocalDate dayDate);

    List<ExceptionalSlot> findAllByDayDateAfterOrderByDayDate(LocalDate dayDate);

    List<ExceptionalSlot> findAll();

    List<ExceptionalSlot> findAllByDayDateBetweenOrderByDayDate(LocalDate start, LocalDate end);
}
