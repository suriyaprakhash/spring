package com.suriyaprakhash.learn.cache_web.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record InventoryEntity(@Id Long id, String name, float price) {
}
