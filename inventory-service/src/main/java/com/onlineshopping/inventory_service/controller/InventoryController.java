package com.onlineshopping.inventory_service.controller;

import com.onlineshopping.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RequestMapping("/api/inventory")
@RestController
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(
            @RequestParam String skuCode,
            @RequestParam Integer quantity) {

        log.info("Checking inventory for skuCode: {} with quantity: {}", skuCode, quantity);

        return inventoryService.isInStock(skuCode, quantity);
    }
}
