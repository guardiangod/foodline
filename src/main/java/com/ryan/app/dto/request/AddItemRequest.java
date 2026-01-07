package com.ryan.app.dto.request;

import com.ryan.app.domain.CatalogType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemRequest {

    private String userId;
    private String outletId;

    /** GROCERY or FOOD */
    private CatalogType catalogType;

    /** productId (for grocery) or menuItemId (for food) */
    private String itemId;

    private int quantity = 1;

    /**
     * If true, override any existing cart content (different outlet or catalog type).
     * If false and there's a conflict, the API returns 409 with details.
     */
    private boolean overrideExistingCart = false;
}
