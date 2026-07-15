package com.ryan.app.service;

import org.springframework.stereotype.Service;

import com.ryan.app.domain.FoodMenuItem;
import com.ryan.app.domain.Restaurant;
import com.ryan.app.persistence.repo.FoodMenuItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodMenuService {

    private final FoodMenuItemRepository foodMenuItemRepository;

    public FoodMenuItem getMenuItem(String itemId, String outletId) {
        var entity = foodMenuItemRepository.findByMenuItemIdAndRestaurant_OutletId(itemId, outletId).orElse(null);
        if (entity == null) {
            return null;
        }

        var restaurant = new Restaurant();
        restaurant.setOutletId(entity.getRestaurant().getOutletId());
        restaurant.setOutletName(entity.getRestaurant().getName());

        return new FoodMenuItem(entity.getMenuItemId(), entity.getName(), entity.getPrice(), restaurant);
    }
}
