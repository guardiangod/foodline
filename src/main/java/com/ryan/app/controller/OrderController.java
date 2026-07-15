package com.ryan.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ryan.app.dto.response.CheckoutResponse;
import com.ryan.app.dto.response.OrderResponse;
import com.ryan.app.dto.response.OrderSummaryResponse;
import com.ryan.app.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestParam(name = "userId") String userId) {
        var resp = orderService.checkout(userId);
        if (resp.getOrderId() == null) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable("orderId") String orderId) {
        var order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    /**
     * List orders.
     * - If userId is provided: list orders for that user.
     * - Else: list all orders.
     */
    @GetMapping
    public ResponseEntity<java.util.List<OrderSummaryResponse>> listOrders(
        @RequestParam(name = "userId", required = false) String userId
    ) {
        return ResponseEntity.ok(orderService.listOrders(userId));
    }
}
