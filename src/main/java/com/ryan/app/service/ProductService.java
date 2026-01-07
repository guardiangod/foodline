package com.ryan.app.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.ryan.app.domain.GroceryProduct;
import com.ryan.app.seedData.SeedData;

@Service
public class ProductService {

    private final List<GroceryProduct> products= SeedData.groceryProducts;

    public GroceryProduct getProduct(String productId, String outletId) {
        return products.stream()
            .filter(groceryProduct ->
                        groceryProduct.getProductId().equals(productId)
                            && groceryProduct.getStore().getOutletId().equals(outletId))
            .findFirst()
            .orElse(null);
    }

}
