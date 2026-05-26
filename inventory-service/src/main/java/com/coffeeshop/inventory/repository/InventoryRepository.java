package com.coffeeshop.inventory.repository;


import com.coffeeshop.inventory.model.InventoryState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryState, Long> {
    Optional<InventoryState> findByProductId(Long productId);
}