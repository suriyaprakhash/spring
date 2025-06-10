package com.suriyaprakhash.cache.inventory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryRepository extends CrudRepository<InventoryEntity, UUID>,
        PagingAndSortingRepository<InventoryEntity, UUID> {
}
