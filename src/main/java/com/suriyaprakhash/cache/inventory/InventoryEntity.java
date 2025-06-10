package com.suriyaprakhash.cache.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity(name = "inventory")
@Data // Generates getters, setters, equals, hashCode, toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class InventoryEntity {


    @Id
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;
    @Column(unique = true) // This makes the 'name' column unique
    private String name;
    private float price;
    private int quantity;

    /**
     * This method is called automatically by JPA before a new entity is persisted.
     * It ensures that a UUID V7 is assigned if the ID is not already set.
     */
    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
             this.id = com.fasterxml.uuid.Generators.timeBasedEpochGenerator().generate();
        }
    }
}
