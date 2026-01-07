package com.ryan.app.domain;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Menu item from a restaurant: price-only (no stock management).
 */
@Getter
@Setter
@NoArgsConstructor
public class FoodMenuItem extends Product {

    /** Restaurant that owns this menu item. */
    private Restaurant restaurant;

    public FoodMenuItem(String itemId, String name, BigDecimal price, Restaurant restaurant) {
        super(itemId, name, price);
        this.restaurant = restaurant;
    }

    public String getOutletId() {
        return restaurant != null ? restaurant.getOutletId() : null;
    }

    public BigDecimal getPrice() {
        return mrp;
    }
}
