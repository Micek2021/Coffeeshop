package com.coffeeshop.inventory;

import com.coffeeshop.inventory.model.InventoryState;
import com.coffeeshop.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) {
        inventoryRepository.save(new InventoryState(null, 1L, "Arabica 1kg", 50));
        inventoryRepository.save(new InventoryState(null, 2L, "Robusta 1kg", 30));
        inventoryRepository.save(new InventoryState(null, 3L, "Espresso 500g", 25));
        inventoryRepository.save(new InventoryState(null, 4L, "Kawa bezkofeinowa 500g", 15));
        inventoryRepository.save(new InventoryState(null, 5L, "Ethiopia Yirgacheffe 250g", 0));
    }
}
