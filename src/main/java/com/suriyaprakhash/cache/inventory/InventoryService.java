package com.suriyaprakhash.cache.inventory;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    private InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void add(@RequestBody Inventory inventory) {
        inventoryRepository.save(new InventoryEntity(inventory.id(), inventory.name(), inventory.price(),
                inventory.quantity()));
    }

    public Inventory getInventory(@RequestParam UUID id) {
        Optional<InventoryEntity> optionalInventoryEntity =
                inventoryRepository.findById(id);
        return optionalInventoryEntity.map(inventoryEntity -> {
            return new Inventory(inventoryEntity.getId(), inventoryEntity.getName(), inventoryEntity.getPrice(),
                    inventoryEntity.getQuantity());
        }).orElseThrow();
    }

    public InventoryEntity getInventoryEntity(@RequestParam UUID id) {
        Optional<InventoryEntity> optionalInventoryEntity =
                inventoryRepository.findById(id);
        return optionalInventoryEntity.orElseThrow();
    }

    @Transactional
    public void remove(@RequestBody UUID id) {
        inventoryRepository.delete(getInventoryEntity(id));
    }
}
