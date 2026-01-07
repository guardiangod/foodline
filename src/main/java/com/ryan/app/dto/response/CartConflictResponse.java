package com.ryan.app.dto.response;

import com.ryan.app.domain.CatalogType;

public record CartConflictResponse(
    String message,
    CatalogType existingCatalogType,
    String existingOutletId,
    CatalogType requestedCatalogType,
    String requestedOutletId,
    boolean overrideRequired
) {}
