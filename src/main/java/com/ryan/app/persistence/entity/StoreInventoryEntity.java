package com.ryan.app.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store_inventory")
@Getter
@Setter
@NoArgsConstructor
public class StoreInventoryEntity {

    @EmbeddedId
    private StoreInventoryId id;

    @MapsId("storeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private OutletEntity store;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private GroceryProductEntity product;

    @Column(name = "stock_qty", nullable = false)
    private long stockQty;

    public StoreInventoryEntity(OutletEntity store, GroceryProductEntity product, long stockQty) {
        this.id = new StoreInventoryId(store.getOutletId(), product.getProductId());
        this.store = store;
        this.product = product;
        this.stockQty = stockQty;
    }
}
