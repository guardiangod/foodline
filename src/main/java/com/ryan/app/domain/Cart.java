package com.ryan.app.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Cart {

    private String cartId;

    /** The cart belongs to exactly one outlet at a time (store OR restaurant). */
    private Outlet outlet;

    /** The cart holds exactly one category at a time (GROCERY or FOOD). */
    private CatalogType catalogType;

    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @JsonIgnore
    private User user;

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
