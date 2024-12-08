package com.onlineshopping.inventory_service.service;

import com.onlineshopping.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
       return inventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, quantity);
    }
    public void updateInventory(String skuCode, Integer quantity) {
        log.info("Updating inventory for skuCode: {} with quantity: {}", skuCode, quantity);
    }
}
