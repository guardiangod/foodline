package com.ryan.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ryan.app.domain.FoodMenuItem;
import com.ryan.app.seedData.SeedData;

@Service
public class FoodMenuService {

    private final List<FoodMenuItem> items = SeedData.foodMenuItems;

    public FoodMenuItem getMenuItem(String itemId, String restaurantId) {
        return items.stream()
            .filter(it -> it.getProductId().equals(itemId) && restaurantId.equals(it.getOutletId()))
            .findFirst()
            .orElse(null);
    }
}
