package com.es.slots.user.entities;

import com.es.slots.pick_up_order.entities.PickUpOrder;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("CLIENT")
/**
 * Represents a client user.
 */
public class Client extends User {

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PickUpOrder> pickUpOrderList = new ArrayList<>();

}
