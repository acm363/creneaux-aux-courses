package com.es.slots.slot.repositories;

import com.es.slots.slot.entities.StandardSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface StandardSlotRepository extends JpaRepository<StandardSlot, Long> {
    Optional<StandardSlot> findByPublicId(String publicId);

    List<StandardSlot> findAll();

    List<StandardSlot> findAllByDayOfWeek(DayOfWeek dayOfWeek);

}
