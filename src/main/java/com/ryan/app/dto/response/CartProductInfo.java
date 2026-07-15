package com.ryan.app.dto.response;

import java.math.BigDecimal;

public record CartProductInfo(CartResponse cart, ProductResponse product, BigDecimal sellingPrice) {
}
