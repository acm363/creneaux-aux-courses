package com.es.slots.pick_up_order.entities;

import com.es.slots.slot.entities.Slot;
import com.es.slots.user.entities.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pick_up_orders", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "slot_id"})
})
// Represents a pick up order.
public class PickUpOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String pickUpOrderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "slot_id", nullable = false)
    @JsonIgnore
    private Slot slot;

    @Column(name = "day_date", nullable = false)
    private LocalDate localDate;
}
