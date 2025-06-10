package com.suriyaprakhash.cache.inventory;

import java.util.UUID;

public record Inventory(UUID id, String name, float price, int quantity) {
}
