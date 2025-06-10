package com.suriyaprakhash.cache.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/inventory")
@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Inventory inventory) {
        inventoryService.add(inventory);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<Inventory> get(@RequestParam(required = false) UUID id) {
        return ResponseEntity.ok(inventoryService.getInventory(id));
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam UUID id) {
        inventoryService.remove(id);
        return ResponseEntity.ok().build();
    }
}
