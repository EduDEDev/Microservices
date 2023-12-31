package com.microservices.inventory_service.utils;
import com.microservices.inventory_service.model.entities.Inventory;
import com.microservices.inventory_service.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading data...");
        if (inventoryRepository.findAll().size() == 0) {
            inventoryRepository.saveAll(
                    List.of(
                            Inventory.builder().sku("000001").quantity(50L).build(),
                            Inventory.builder().sku("000002").quantity(70L).build(),
                            Inventory.builder().sku("000003").quantity(80L).build(),
                            Inventory.builder().sku("000004").quantity(0L).build()
                    )
            );
        }
        log.info("Data loaded.");
    }
}