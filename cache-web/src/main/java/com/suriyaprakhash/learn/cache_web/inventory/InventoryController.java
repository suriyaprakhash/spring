package com.suriyaprakhash.learn.cache_web.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/inventory")
@RestController
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Inventory inventory) {
        inventoryService.add(inventory);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<Inventory> get(@RequestParam(required = false) long id) {
        return ResponseEntity.ok(inventoryService.getInventory(id));
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam long id) {
        inventoryService.remove(id);
        return ResponseEntity.ok().build();
    }
}
