package com.ryan.app.exception;

import com.ryan.app.domain.Cart;
import com.ryan.app.domain.CatalogType;

import lombok.Getter;

@Getter
public class CartConflictException extends RuntimeException {

    private final CatalogType existingCatalogType;
    private final String existingOutletId;

    private final CatalogType requestedCatalogType;
    private final String requestedOutletId;

    public CartConflictException(Cart existingCart, CatalogType requestedCatalogType, String requestedOutletId) {
        super("Cart already contains items from a different category/outlet. Override required.");
        this.existingCatalogType = existingCart.getCatalogType();
        this.existingOutletId = existingCart.getOutlet() != null ? existingCart.getOutlet().getOutletId() : null;
        this.requestedCatalogType = requestedCatalogType;
        this.requestedOutletId = requestedOutletId;
    }
}
