package com.es.slots.pick_up_order.repositories;

import com.es.slots.pick_up_order.entities.PickUpOrder;
import com.es.slots.user.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Represents a pick up order repository
public interface PickUpOrderRepository extends JpaRepository<PickUpOrder, Long> {
  
    Optional<PickUpOrder> findByPickUpOrderId(String pickUpOrderId);

    List<PickUpOrder> findAllByClient(Client client);
}
