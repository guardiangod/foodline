package com.ryan.app.persistence.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.app.persistence.entity.StoreInventoryEntity;
import com.ryan.app.persistence.entity.StoreInventoryId;

public interface StoreInventoryRepository extends JpaRepository<StoreInventoryEntity, StoreInventoryId> {

    Optional<StoreInventoryEntity> findByStore_OutletIdAndProduct_ProductId(String storeId, String productId);

    List<StoreInventoryEntity> findByProduct_ProductIdAndStockQtyGreaterThanEqual(String productId, long stockQty);
}
