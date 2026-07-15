package com.ryan.app.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "food_menu_items")
@Getter
@Setter
@NoArgsConstructor
public class FoodMenuItemEntity {

    @Id
    @Column(name = "menu_item_id", length = 50, nullable = false)
    private String menuItemId;

    @Column(name = "name", length = 250, nullable = false)
    private String name;

    @Column(name = "price", precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private OutletEntity restaurant;

    public FoodMenuItemEntity(String menuItemId, String name, BigDecimal price, OutletEntity restaurant) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }
}
