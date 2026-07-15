package com.ryan.app.persistence.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.app.persistence.entity.FoodMenuItemEntity;

public interface FoodMenuItemRepository extends JpaRepository<FoodMenuItemEntity, String> {

    Optional<FoodMenuItemEntity> findByMenuItemIdAndRestaurant_OutletId(String menuItemId, String restaurantId);
}
