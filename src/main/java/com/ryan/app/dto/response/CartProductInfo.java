package com.ryan.app.dto.response;

import java.math.BigDecimal;

import com.ryan.app.domain.Cart;
import com.ryan.app.domain.Product;

public record CartProductInfo(Cart cart, Product product, BigDecimal sellingPrice) {
}
