package com.ryan.app.seedData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.ryan.app.domain.Cart;
import com.ryan.app.domain.CatalogType;
import com.ryan.app.domain.FoodMenuItem;
import com.ryan.app.domain.GroceryProduct;
import com.ryan.app.domain.GroceryStore;
import com.ryan.app.domain.Restaurant;
import com.ryan.app.domain.User;

public class SeedData {

    // In-memory data store (mutable on purpose for this kata).
    public static final Map<String, Cart> cartForUsers = new ConcurrentHashMap<>();

    public static final GroceryStore store101 = createStore("Fresh Picks", "store101");
    public static final GroceryStore store102 = createStore("Natural Choice", "store102");

    public static final Restaurant restaurant201 = createRestaurant("Wok & Roll", "rest201");
    public static final Restaurant restaurant202 = createRestaurant("Pasta Palace", "rest202");

    public static final User user101 = createUser("user101", "John", "Doe");

    public static final List<User> users = Arrays.asList(user101);

    public static final List<GroceryProduct> groceryProducts = Arrays.asList(
        createGroceryProduct("Wheat Bread", "product101", store101),
        createGroceryProduct("Spinach", "product102", store101),
        createGroceryProduct("Crackers", "product103", store101),
        createGroceryProduct("Milk", "product104", store102)
    );

    public static final List<FoodMenuItem> foodMenuItems = Arrays.asList(
        createFoodMenuItem("Chicken Fried Rice", "menu101", BigDecimal.valueOf(8.90), restaurant201),
        createFoodMenuItem("Beef Noodles", "menu102", BigDecimal.valueOf(10.90), restaurant201),
        createFoodMenuItem("Carbonara", "menu201", BigDecimal.valueOf(12.50), restaurant202)
    );

    static {
        // Initialize carts (empty carts, no category selected yet).
        cartForUsers.put("user101", createEmptyCartForUser(user101, "cart101"));
    }

    public static Cart createEmptyCartForUser(User user, String cartId) {
        return Cart.builder()
            .cartId(cartId)
            .user(user)
            .catalogType(null)
            .outlet(null)
            .items(new ArrayList<>())
            .build();
    }

    public static GroceryStore createStore(String outletName, String storeId) {
        return GroceryStore.builder()
            .name(outletName)
            .outletId(storeId)
            .build();
    }

    public static Restaurant createRestaurant(String outletName, String restaurantId) {
        return Restaurant.builder()
            .name(outletName)
            .outletId(restaurantId)
            .build();
    }

    public static User createUser(String userId, String firstName, String lastName) {
        return User.builder()
            .userId(userId)
            .firstName(firstName)
            .lastName(lastName)
            .email(firstName + "." + lastName + "@gmail.com")
            .phoneNumber(String.valueOf(getRandomNumberUsingNextInt(100000000, 900000000)))
            .build();
    }

    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private static GroceryProduct createGroceryProduct(String productName,
                                                       String productId, GroceryStore store) {
        return GroceryProduct.builder()
            .productName(productName)
            .productId(productId)
            .mrp(BigDecimal.valueOf(10.5))
            .weight(BigDecimal.valueOf(500))
            .expiryDate(10)
            .threshold(10)
            .availableStock(100)
            .store(store)
            .discount(BigDecimal.valueOf(1))
            .sellingPrice(BigDecimal.valueOf(9.5))
            .build();
    }

    private static FoodMenuItem createFoodMenuItem(String name, String itemId, BigDecimal price, Restaurant restaurant) {
        return new FoodMenuItem(itemId, name, price, restaurant);
    }
}
