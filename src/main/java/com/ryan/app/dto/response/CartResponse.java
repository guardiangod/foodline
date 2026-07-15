package com.ryan.app.dto.response;

import java.util.ArrayList;
import java.util.List;

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
public class CartResponse {

    private String cartId;
    private String userId;
    private CatalogType catalogType;
    private OutletResponse outlet;

    @Builder.Default
    private List<CartItemResponse> items = new ArrayList<>();
}
