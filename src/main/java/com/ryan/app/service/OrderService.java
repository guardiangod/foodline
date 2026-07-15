package com.ryan.app.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ryan.app.domain.CatalogType;
import com.ryan.app.dto.response.CheckoutResponse;
import com.ryan.app.dto.response.OrderItemResponse;
import com.ryan.app.persistence.entity.OrderEntity;
import com.ryan.app.persistence.entity.OrderItemEntity;
import com.ryan.app.persistence.entity.OrderStatus;
import com.ryan.app.persistence.repo.CartRepository;
import com.ryan.app.persistence.repo.OutletRepository;
import com.ryan.app.persistence.repo.OrderRepository;
import com.ryan.app.persistence.repo.StoreInventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OutletRepository outletRepository;
    private final StoreInventoryRepository storeInventoryRepository;

    /**
     * Checkout:
     * - create order from cart
     * - assign delivery partner/outlet depending on catalogType
     * - fulfill:
     *   - grocery: reduce stock only in the assigned store
     *   - food: no stock changes
     * - clear cart
     */
    @Transactional
    public CheckoutResponse checkout(String userId) {
        var cart = cartRepository.findByUser_UserId(userId).orElse(null);
        if (cart == null) {
            return CheckoutResponse.notOk("Cart not found for user: " + userId);
        }
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return CheckoutResponse.notOk("Cart is empty.");
        }
        if (cart.getCatalogType() == null || cart.getOutlet() == null) {
            return CheckoutResponse.notOk("Cart is incomplete (missing outlet/type).");
        }

        var order = new OrderEntity();
        order.setOrderId("ord_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        order.setUser(cart.getUser());
        order.setOutlet(cart.getOutlet());
        order.setCatalogType(cart.getCatalogType());
        order.setStatus(OrderStatus.CREATED);

        // Assignment
        var assignedOutlet = assignOutlet(cart);
        order.setAssignedOutlet(assignedOutlet);

        // Copy items
        for (var ci : cart.getItems()) {
            order.getItems().add(new OrderItemEntity(
                order,
                ci.getItemId(),
                ci.getName(),
                ci.getUnitPrice(),
                ci.getQuantity(),
                ci.getCatalogType()
            ));
        }

        // Fulfillment
        if (order.getCatalogType() == CatalogType.GROCERY) {
            // Reduce stock only from assigned store.
            for (var item : order.getItems()) {
                var inv = storeInventoryRepository.findByStore_OutletIdAndProduct_ProductId(
                    order.getAssignedOutlet().getOutletId(),
                    item.getItemId()
                ).orElse(null);

                if (inv == null) {
                    // Not found in assigned store -> fail fast.
                    return CheckoutResponse.notOk("Inventory not found for product " + item.getItemId()
                        + " in assigned store " + order.getAssignedOutlet().getOutletId());
                }
                if (inv.getStockQty() < item.getQuantity()) {
                    return CheckoutResponse.notOk("Insufficient stock for product " + item.getItemId()
                        + " in assigned store " + order.getAssignedOutlet().getOutletId());
                }
                inv.setStockQty(inv.getStockQty() - item.getQuantity());
                storeInventoryRepository.save(inv);
            }
        }

        order.setStatus(OrderStatus.CONFIRMED);
        var saved = orderRepository.save(order);

        // Clear cart (keep outlet/type? requirement implies after checkout cart can be empty; safest is clear all)
        cart.getItems().clear();
        cart.setCatalogType(null);
        cart.setOutlet(null);
        cartRepository.save(cart);

        // Response
        var resp = CheckoutResponse.builder()
            .orderId(saved.getOrderId())
            .userId(saved.getUser().getUserId())
            .outletId(saved.getOutlet().getOutletId())
            .assignedOutletId(saved.getAssignedOutlet().getOutletId())
            .catalogType(saved.getCatalogType())
            .status(saved.getStatus())
            .message("Order confirmed")
            .build();

        for (var oi : saved.getItems()) {
            resp.getItems().add(OrderItemResponse.builder()
                .itemId(oi.getItemId())
                .name(oi.getName())
                .unitPrice(oi.getUnitPrice())
                .quantity(oi.getQuantity())
                .catalogType(oi.getCatalogType())
                .build());
        }

        return resp;
    }

    private com.ryan.app.persistence.entity.OutletEntity assignOutlet(com.ryan.app.persistence.entity.CartEntity cart) {
        if (cart.getCatalogType() == CatalogType.FOOD) {
            // Food: restaurant is user-selected (cart outlet).
            return cart.getOutlet();
        }

        // Grocery: "nearest store" - for now choose a store that can fulfill ALL items.
        // (Deterministic: first storeId in sorted list that has sufficient stock for each item.)
        // If no alternative found, fall back to cart outlet.
        var candidateStores = outletRepository.findAll().stream()
            .filter(o -> o.getOutletType() != null && o.getOutletType().name().contains("GROCERY"))
            .sorted((a, b) -> a.getOutletId().compareTo(b.getOutletId()))
            .toList();

        for (var store : candidateStores) {
            boolean ok = true;
            for (var item : cart.getItems()) {
                var inv = storeInventoryRepository.findByStore_OutletIdAndProduct_ProductId(store.getOutletId(), item.getItemId()).orElse(null);
                if (inv == null || inv.getStockQty() < item.getQuantity()) {
                    ok = false;
                    break;
                }
            }
            if (ok) return store;
        }
        return cart.getOutlet();
    }
}
