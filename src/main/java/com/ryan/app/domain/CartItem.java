package com.ryan.app.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Snapshot of what the user is buying at the time it is added to cart.
 * (Price can change later in the catalog; cart keeps the unitPrice.)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String itemId;
    private String name;
    private BigDecimal unitPrice;
    private int quantity;
    private CatalogType catalogType;

}
