package com.ryan.app.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ryan.app.domain.CatalogType;
import com.ryan.app.persistence.entity.CartEntity;
import com.ryan.app.persistence.entity.FoodMenuItemEntity;
import com.ryan.app.persistence.entity.GroceryProductEntity;
import com.ryan.app.persistence.entity.OutletEntity;
import com.ryan.app.persistence.entity.OutletType;
import com.ryan.app.persistence.entity.StoreInventoryEntity;
import com.ryan.app.persistence.entity.UserEntity;
import com.ryan.app.persistence.repo.CartRepository;
import com.ryan.app.persistence.repo.FoodMenuItemRepository;
import com.ryan.app.persistence.repo.GroceryProductRepository;
import com.ryan.app.persistence.repo.OutletRepository;
import com.ryan.app.persistence.repo.StoreInventoryRepository;
import com.ryan.app.persistence.repo.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Seeds minimal demo data into H2.
 *
 * NOTE: This project previously used in-memory SeedData; now we persist via JPA/H2.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OutletRepository outletRepository;
    private final GroceryProductRepository groceryProductRepository;
    private final FoodMenuItemRepository foodMenuItemRepository;
    private final StoreInventoryRepository storeInventoryRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        // Users
        var user101 = userRepository.save(new UserEntity("user101", "Ryan"));

        // Outlets
        var store101 = outletRepository.save(new OutletEntity("store101", "FreshMart (Store 101)", OutletType.GROCERY_STORE));
        var store102 = outletRepository.save(new OutletEntity("store102", "DailyGrocer (Store 102)", OutletType.GROCERY_STORE));
        var restaurant201 = outletRepository.save(new OutletEntity("rest201", "Noodle House (Rest 201)", OutletType.RESTAURANT));

        // Grocery products (inventory-driven)
        var apple = groceryProductRepository.save(new GroceryProductEntity("g001", "Apple", new BigDecimal("1.20"), store101));
        var milk = groceryProductRepository.save(new GroceryProductEntity("g002", "Milk", new BigDecimal("3.50"), store101));

        // Also make same products available in store102 for "nearest store" demo.
        var apple2 = groceryProductRepository.save(new GroceryProductEntity("g001_s2", "Apple", new BigDecimal("1.20"), store102));
        var milk2 = groceryProductRepository.save(new GroceryProductEntity("g002_s2", "Milk", new BigDecimal("3.50"), store102));

        storeInventoryRepository.save(new StoreInventoryEntity(store101, apple, 50));
        storeInventoryRepository.save(new StoreInventoryEntity(store101, milk, 20));
        storeInventoryRepository.save(new StoreInventoryEntity(store102, apple2, 10));
        storeInventoryRepository.save(new StoreInventoryEntity(store102, milk2, 5));

        // Food menu items (menu-driven; no stock updates)
        foodMenuItemRepository.save(new FoodMenuItemEntity("f101", "Beef Noodles", new BigDecimal("8.90"), restaurant201));
        foodMenuItemRepository.save(new FoodMenuItemEntity("f102", "Fried Dumplings", new BigDecimal("5.50"), restaurant201));

        // Create an empty cart for user101
        var cart = new CartEntity("cart_" + user101.getUserId(), user101);
        cart.setCatalogType(null);
        cart.setOutlet(null);
        cartRepository.save(cart);
    }
}
