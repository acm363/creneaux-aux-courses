package com.es.slots.public_holiday.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "publicholidays")

// Represents a public holiday entity.
public class PublicHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    // We don't want to have two public holidays on the same date.
    @Column(unique = true)
    private LocalDate date;

    @Column(unique = true)
    private String publicId;
}
