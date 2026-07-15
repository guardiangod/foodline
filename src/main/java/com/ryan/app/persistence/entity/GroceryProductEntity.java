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
@Table(name = "grocery_products")
@Getter
@Setter
@NoArgsConstructor
public class GroceryProductEntity {

    @Id
    @Column(name = "product_id", length = 50, nullable = false)
    private String productId;

    @Column(name = "name", length = 250, nullable = false)
    private String name;

    @Column(name = "price", precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private OutletEntity store;

    public GroceryProductEntity(String productId, String name, BigDecimal price, OutletEntity store) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.store = store;
    }
}
