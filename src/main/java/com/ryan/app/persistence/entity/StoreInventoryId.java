package com.ryan.app.persistence.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StoreInventoryId implements Serializable {

    @Column(name = "store_id", length = 50, nullable = false)
    private String storeId;

    @Column(name = "product_id", length = 50, nullable = false)
    private String productId;

    public String getStoreId() {
        return storeId;
    }

    public String getProductId() {
        return productId;
    }
}
