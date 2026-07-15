package com.ryan.app.dto.response;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ryan.app.domain.CatalogType;
import com.ryan.app.persistence.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String orderId;
    private String userId;

    /** Outlet selected in cart. */
    private String outletId;

    /** Assigned outlet for fulfillment/delivery partner. */
    private String assignedOutletId;

    private CatalogType catalogType;
    private OrderStatus status;
    private OffsetDateTime createdAt;

    @Builder.Default
    private List<OrderItemResponse> items = new ArrayList<>();
}
