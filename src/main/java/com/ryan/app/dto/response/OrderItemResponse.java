package com.ryan.app.dto.response;

import java.math.BigDecimal;

import com.ryan.app.domain.CatalogType;

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
public class OrderItemResponse {

    private String itemId;
    private String name;
    private BigDecimal unitPrice;
    private int quantity;
    private CatalogType catalogType;
}
