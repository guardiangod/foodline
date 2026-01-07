package com.ryan.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ryan.app.dto.response.CartConflictResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CartConflictException.class)
    public ResponseEntity<CartConflictResponse> handleCartConflict(CartConflictException ex) {
        var body = new CartConflictResponse(
            "Your cart already has items from another category/outlet. Set overrideExistingCart=true to proceed and replace the cart.",
            ex.getExistingCatalogType(),
            ex.getExistingOutletId(),
            ex.getRequestedCatalogType(),
            ex.getRequestedOutletId(),
            true
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
