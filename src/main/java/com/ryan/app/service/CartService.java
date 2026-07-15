package com.ryan.app.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ryan.app.domain.Cart;
import com.ryan.app.domain.CartItem;
import com.ryan.app.domain.CatalogType;
import com.ryan.app.domain.FoodMenuItem;
import com.ryan.app.domain.GroceryProduct;
import com.ryan.app.domain.GroceryStore;
import com.ryan.app.domain.Outlet;
import com.ryan.app.domain.Restaurant;
import com.ryan.app.dto.request.AddItemRequest;
import com.ryan.app.dto.request.AddProductRequest;
import com.ryan.app.dto.response.AddItemResponse;
import com.ryan.app.dto.response.CartProductInfo;
import com.ryan.app.exception.CartConflictException;
import com.ryan.app.persistence.entity.CartEntity;
import com.ryan.app.persistence.entity.CartItemEntity;
import com.ryan.app.persistence.repo.CartRepository;
import com.ryan.app.persistence.repo.OutletRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserService userService;
    private final ProductService productService;
    private final FoodMenuService foodMenuService;

    private final CartRepository cartRepository;
    private final OutletRepository outletRepository;

    /**
     * Backwards-compatible grocery-only endpoint support.
     */
    @Transactional
    public CartProductInfo addProductToCartForUser(AddProductRequest addProductRequest) {
        var req = new AddItemRequest();
        req.setUserId(addProductRequest.getUserId());
        req.setOutletId(addProductRequest.getOutletId());
        req.setItemId(addProductRequest.getProductId());
        req.setCatalogType(CatalogType.GROCERY);
        req.setQuantity(1);
        req.setOverrideExistingCart(false);

        var resp = addItemToCartForUser(req);

        // Legacy response shape: include the resolved grocery product.
        var product = productService.getProduct(addProductRequest.getProductId(), addProductRequest.getOutletId());
        return new CartProductInfo(resp.cart(), product, product != null ? product.getSellingPrice() : null);
    }

    @Transactional
    public AddItemResponse addItemToCartForUser(AddItemRequest request) {
        var user = userService.fetchUserById(request.getUserId());
        if (user == null) {
            return new AddItemResponse(null, null);
        }

        var cart = cartRepository.findByUser_UserId(request.getUserId())
            .orElseGet(() -> cartRepository.save(new CartEntity("cart_" + request.getUserId(), user)));

        var requestedType = request.getCatalogType();
        var requestedOutletId = request.getOutletId();

        boolean cartHasType = cart.getCatalogType() != null;
        boolean cartHasOutlet = cart.getOutlet() != null;

        boolean conflict = (cartHasType && requestedType != cart.getCatalogType())
            || (cartHasOutlet && requestedOutletId != null && !requestedOutletId.equals(cart.getOutlet().getOutletId()));

        if (conflict && !request.isOverrideExistingCart()) {
            throw new CartConflictException(toDomain(cart), requestedType, requestedOutletId);
        }

        if (conflict && request.isOverrideExistingCart()) {
            cart.getItems().clear();
            cart.setCatalogType(null);
            cart.setOutlet(null);
        }

        if (cart.getCatalogType() == null) {
            cart.setCatalogType(requestedType);
        }
        if (cart.getOutlet() == null) {
            var outlet = outletRepository.findById(requestedOutletId).orElse(null);
            if (outlet == null) {
                return new AddItemResponse(null, null);
            }
            cart.setOutlet(outlet);
        }

        var qty = request.getQuantity() <= 0 ? 1 : request.getQuantity();

        String name;
        java.math.BigDecimal unitPrice;

        if (requestedType == CatalogType.GROCERY) {
            GroceryProduct gp = productService.getProduct(request.getItemId(), requestedOutletId);
            if (gp == null) return new AddItemResponse(null, null);
            name = gp.getProductName();
            unitPrice = gp.getSellingPrice() != null ? gp.getSellingPrice() : gp.getMrp();
        } else {
            FoodMenuItem mi = foodMenuService.getMenuItem(request.getItemId(), requestedOutletId);
            if (mi == null) return new AddItemResponse(null, null);
            name = mi.getProductName();
            unitPrice = mi.getPrice();
        }

        Optional<CartItemEntity> existing = cart.getItems().stream()
            .filter(i -> i.getItemId().equals(request.getItemId()) && i.getCatalogType() == requestedType)
            .findFirst();

        CartItemEntity addedEntity;
        if (existing.isPresent()) {
            addedEntity = existing.get();
            addedEntity.setQuantity(addedEntity.getQuantity() + qty);
        } else {
            addedEntity = new CartItemEntity(cart, request.getItemId(), name, unitPrice, qty, requestedType);
            cart.getItems().add(addedEntity);
        }

        cartRepository.save(cart);

        var domainCart = toDomain(cart);
        var addedItem = CartItem.builder()
            .itemId(addedEntity.getItemId())
            .name(addedEntity.getName())
            .unitPrice(addedEntity.getUnitPrice())
            .quantity(addedEntity.getQuantity())
            .catalogType(addedEntity.getCatalogType())
            .build();

        return new AddItemResponse(domainCart, addedItem);
    }

    @Transactional(readOnly = true)
    public Cart getCartForUser(String userId) {
        return cartRepository.findByUser_UserId(userId).map(this::toDomain).orElse(null);
    }

    private Cart toDomain(CartEntity entity) {
        if (entity == null) return null;

        Outlet outlet = null;
        if (entity.getOutlet() != null) {
            if (entity.getOutlet().getOutletType() != null && entity.getOutlet().getOutletType().name().contains("RESTAURANT")) {
                var r = new Restaurant();
                r.setOutletId(entity.getOutlet().getOutletId());
                r.setOutletName(entity.getOutlet().getName());
                outlet = r;
            } else {
                var s = new GroceryStore();
                s.setOutletId(entity.getOutlet().getOutletId());
                s.setOutletName(entity.getOutlet().getName());
                outlet = s;
            }
        }

        var cart = Cart.builder()
            .cartId(entity.getCartId())
            .userId(entity.getUser().getUserId())
            .catalogType(entity.getCatalogType())
            .outlet(outlet)
            .build();

        if (entity.getItems() != null) {
            for (var item : entity.getItems()) {
                cart.getItems().add(CartItem.builder()
                    .itemId(item.getItemId())
                    .name(item.getName())
                    .unitPrice(item.getUnitPrice())
                    .quantity(item.getQuantity())
                    .catalogType(item.getCatalogType())
                    .build());
            }
        }
        return cart;
    }
}
