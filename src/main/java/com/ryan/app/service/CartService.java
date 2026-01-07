package com.ryan.app.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ryan.app.domain.Cart;
import com.ryan.app.domain.CartItem;
import com.ryan.app.domain.CatalogType;
import com.ryan.app.domain.FoodMenuItem;
import com.ryan.app.domain.GroceryProduct;
import com.ryan.app.domain.Outlet;
import com.ryan.app.domain.User;
import com.ryan.app.dto.request.AddItemRequest;
import com.ryan.app.dto.request.AddProductRequest;
import com.ryan.app.dto.response.AddItemResponse;
import com.ryan.app.dto.response.CartProductInfo;
import com.ryan.app.exception.CartConflictException;
import com.ryan.app.seedData.SeedData;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserService userService;
    private final ProductService productService;
    private final FoodMenuService foodMenuService;

    private final Map<String, Cart> userCarts = SeedData.cartForUsers;

    /**
     * Backwards-compatible grocery-only endpoint support.
     */
    public CartProductInfo addProductToCartForUser(AddProductRequest addProductRequest) {
        var req = new AddItemRequest();
        req.setUserId(addProductRequest.getUserId());
        req.setOutletId(addProductRequest.getOutletId());
        req.setCatalogType(CatalogType.GROCERY);
        req.setItemId(addProductRequest.getProductId());
        req.setQuantity(1);
        req.setOverrideExistingCart(false);

        var response = addItemToCartForUser(req);
        // Keep legacy response shape.
        GroceryProduct product = productService.getProduct(addProductRequest.getProductId(), addProductRequest.getOutletId());
        return new CartProductInfo(response.cart(), product, product != null ? product.getSellingPrice() : null);
    }

    public AddItemResponse addItemToCartForUser(AddItemRequest request) {
        User user = userService.fetchUserById(request.getUserId());
        Cart cart = fetchOrCreateCartForUser(user);

        validateOrOverrideCart(cart, request);

        CartItem added = switch (request.getCatalogType()) {
            case GROCERY -> addGroceryItem(cart, request);
            case FOOD -> addFoodItem(cart, request);
        };

        return new AddItemResponse(cart, added);
    }

    public Cart getCartForUser(String userId) {
        User user = userService.fetchUserById(userId);
        return fetchOrCreateCartForUser(user);
    }

    private Cart fetchOrCreateCartForUser(User user) {
        return userCarts.computeIfAbsent(user.getUserId(), uid -> SeedData.createEmptyCartForUser(user, "cart-" + uid));
    }

    private void validateOrOverrideCart(Cart cart, AddItemRequest request) {
        boolean cartInitialized = cart.getCatalogType() != null && cart.getOutlet() != null && !cart.isEmpty();

        if (!cartInitialized) {
            // First item defines the cart.
            cart.setCatalogType(request.getCatalogType());
            cart.setOutlet(resolveOutlet(request));
            return;
        }

        boolean sameType = cart.getCatalogType() == request.getCatalogType();
        boolean sameOutlet = cart.getOutlet() != null && cart.getOutlet().getOutletId().equals(request.getOutletId());

        if (sameType && sameOutlet) {
            return;
        }

        if (!request.isOverrideExistingCart()) {
            throw new CartConflictException(cart, request.getCatalogType(), request.getOutletId());
        }

        // Override: clear & re-initialize
        cart.getItems().clear();
        cart.setCatalogType(request.getCatalogType());
        cart.setOutlet(resolveOutlet(request));
    }

    private Outlet resolveOutlet(AddItemRequest request) {
        // For this kata we only support in-memory seed outlets.
        // In a DB-backed version, you'd fetch from repository.
        if (request.getCatalogType() == CatalogType.GROCERY) {
            if (SeedData.store101.getOutletId().equals(request.getOutletId())) return SeedData.store101;
            if (SeedData.store102.getOutletId().equals(request.getOutletId())) return SeedData.store102;
        } else {
            if (SeedData.restaurant201.getOutletId().equals(request.getOutletId())) return SeedData.restaurant201;
            if (SeedData.restaurant202.getOutletId().equals(request.getOutletId())) return SeedData.restaurant202;
        }
        return null;
    }

    private CartItem addGroceryItem(Cart cart, AddItemRequest request) {
        GroceryProduct product = productService.getProduct(request.getItemId(), request.getOutletId());
        if (product == null) {
            throw new IllegalArgumentException("Grocery product not found for outletId=" + request.getOutletId() + ", productId=" + request.getItemId());
        }

        CartItem item = CartItem.builder()
            .itemId(product.getProductId())
            .name(product.getProductName())
            .unitPrice(product.getSellingPrice())
            .quantity(Math.max(1, request.getQuantity()))
            .catalogType(CatalogType.GROCERY)
            .build();

        cart.getItems().add(item);
        return item;
    }

    private CartItem addFoodItem(Cart cart, AddItemRequest request) {
        FoodMenuItem menuItem = foodMenuService.getMenuItem(request.getItemId(), request.getOutletId());
        if (menuItem == null) {
            throw new IllegalArgumentException("Food menu item not found for outletId=" + request.getOutletId() + ", itemId=" + request.getItemId());
        }

        CartItem item = CartItem.builder()
            .itemId(menuItem.getProductId())
            .name(menuItem.getProductName())
            .unitPrice(menuItem.getPrice())
            .quantity(Math.max(1, request.getQuantity()))
            .catalogType(CatalogType.FOOD)
            .build();

        cart.getItems().add(item);
        return item;
    }
}
