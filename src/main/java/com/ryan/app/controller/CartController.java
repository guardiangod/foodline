package com.ryan.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ryan.app.domain.Cart;
import com.ryan.app.dto.request.AddProductRequest;
import com.ryan.app.dto.response.CartProductInfo;
import com.ryan.app.service.CartService;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/product")
    public ResponseEntity<CartProductInfo> addProductToCart(@RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.ok(cartService.addProductToCartForUser(addProductRequest));
    }

    @GetMapping("/view")
    public ResponseEntity<Cart> viewCart(@RequestParam(name = "userId") String userId) {
        return ResponseEntity.ok(cartService.getCartForUser(userId));
    }
}
