package com.ryan.app.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ryan.app.domain.Cart;
import com.ryan.app.domain.GroceryProduct;
import com.ryan.app.domain.User;
import com.ryan.app.dto.request.AddProductRequest;
import com.ryan.app.dto.response.CartProductInfo;
import com.ryan.app.seedData.SeedData;

@Service
@RequiredArgsConstructor
public class CartService {

    private final Map<String,Cart> userCarts= SeedData.cartForUsers;
    private final UserService userService;
    private final ProductService productService;

    public CartProductInfo addProductToCartForUser(AddProductRequest addProductRequest) {
        User user=userService.fetchUserById(addProductRequest.getUserId());
        Cart cart = fetchCartForUser(user);
        GroceryProduct product = productService.getProduct(addProductRequest.getProductId(),
                                                           addProductRequest.getOutletId());
        cart.getProducts().add(product);
        return new CartProductInfo(cart, product, product.getSellingPrice());
    }

    public Cart getCartForUser(String userId) {
        User user=userService.fetchUserById(userId);
        return fetchCartForUser(user);
    }

    private Cart fetchCartForUser(User user) {
        return userCarts.get(user.getUserId());
    }

}
