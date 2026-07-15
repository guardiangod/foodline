package com.ryan.app.dto.response;

import java.time.OffsetDateTime;

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
public class OrderSummaryResponse {

    private String orderId;
    private String userId;
    private String outletId;
    private String assignedOutletId;
    private CatalogType catalogType;
    private OrderStatus status;
    private OffsetDateTime createdAt;
    private int itemCount;
}
