package com.suriyaprakhash.learn.cache_web.inventory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends CrudRepository<InventoryEntity, Long>,
        PagingAndSortingRepository<InventoryEntity, Long> {
}
