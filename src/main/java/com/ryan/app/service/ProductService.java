package com.ryan.app.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.ryan.app.domain.GroceryProduct;
import com.ryan.app.domain.GroceryStore;
import com.ryan.app.persistence.entity.OutletType;
import com.ryan.app.persistence.repo.GroceryProductRepository;
import com.ryan.app.persistence.repo.StoreInventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final GroceryProductRepository groceryProductRepository;
    private final StoreInventoryRepository storeInventoryRepository;

    public GroceryProduct getProduct(String productId, String outletId) {
        var productEntity = groceryProductRepository.findByProductIdAndStore_OutletId(productId, outletId).orElse(null);
        if (productEntity == null) {
            return null;
        }

        var stock = storeInventoryRepository
            .findByStore_OutletIdAndProduct_ProductId(outletId, productEntity.getProductId())
            .map(inv -> (int) inv.getStockQty())
            .orElse(0);

        var store = new GroceryStore();
        store.setOutletId(productEntity.getStore().getOutletId());
        store.setOutletName(productEntity.getStore().getName());
        // existing domain has no outletType field; keep as GroceryStore.

        var gp = new GroceryProduct();
        gp.setProductId(productEntity.getProductId());
        gp.setProductName(productEntity.getName());
        gp.setMrp(productEntity.getPrice());
        gp.setSellingPrice(productEntity.getPrice());
        gp.setAvailableStock(stock);
        gp.setStore(store);
        gp.setDiscount(BigDecimal.ZERO);
        return gp;
    }
}
