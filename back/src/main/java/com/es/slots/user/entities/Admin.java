package com.es.slots.user.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
/**
 * Represents an admin user.
 */
public class Admin extends User {
}
