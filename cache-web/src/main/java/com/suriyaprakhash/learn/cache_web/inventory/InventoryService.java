package com.suriyaprakhash.learn.cache_web.inventory;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional
    public void add(@RequestBody Inventory inventory) {
        inventoryRepository.save(new InventoryEntity(inventory.id(), inventory.name(), inventory.price()));
    }

    public Inventory getInventory(@RequestParam Long id) {
        Optional<InventoryEntity> optionalInventoryEntity =
                inventoryRepository.findById(id);
        return optionalInventoryEntity.map(inventoryEntity -> {
            return new Inventory(inventoryEntity.id(), inventoryEntity.name(), inventoryEntity.price());
        }).orElseThrow();
    }

    public InventoryEntity getInventoryEntity(@RequestParam Long id) {
        Optional<InventoryEntity> optionalInventoryEntity =
                inventoryRepository.findById(id);
        return optionalInventoryEntity.orElseThrow();
    }

    @Transactional
    public void remove(@RequestBody Long id) {
        inventoryRepository.delete(getInventoryEntity(id));
    }
}
