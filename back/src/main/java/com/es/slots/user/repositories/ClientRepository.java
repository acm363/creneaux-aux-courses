package com.es.slots.user.repositories;

import com.es.slots.user.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Client entity.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
}
