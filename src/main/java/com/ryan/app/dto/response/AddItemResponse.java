package com.ryan.app.dto.response;

import com.ryan.app.domain.Cart;
import com.ryan.app.domain.CartItem;

public record AddItemResponse(Cart cart, CartItem addedItem) {}
