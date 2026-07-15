package com.ryan.app.persistence.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.app.persistence.entity.GroceryProductEntity;

public interface GroceryProductRepository extends JpaRepository<GroceryProductEntity, String> {

    Optional<GroceryProductEntity> findByProductIdAndStore_OutletId(String productId, String storeId);
}
