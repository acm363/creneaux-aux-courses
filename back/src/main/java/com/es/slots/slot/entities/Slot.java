package com.es.slots.slot.entities;

import com.es.slots.pick_up_order.entities.PickUpOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Table(name = "slots")


public abstract class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String publicId = UUID.randomUUID().toString();

    @NotNull(message = "Start hour is required")
    @Schema(type = "String", pattern = "08:00")
    private LocalTime startHour;

    @NotNull(message = "End hour is required")
    @Schema(type = "String", pattern = "20:00")
    private LocalTime endHour;

    @NotNull(message = "Capacity is required")
    private int capacity;

    @OneToMany(mappedBy = "slot", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PickUpOrder> pickUpOrderList = new ArrayList<>();
}
