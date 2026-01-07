package com.ryan.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.app.domain.Cart;
import com.ryan.app.domain.CatalogType;
import com.ryan.app.domain.GroceryStore;
import com.ryan.app.dto.request.AddItemRequest;
import com.ryan.app.dto.response.AddItemResponse;
import com.ryan.app.exception.ApiExceptionHandler;
import com.ryan.app.exception.CartConflictException;
import com.ryan.app.service.CartService;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(CartController.class)
@Import(ApiExceptionHandler.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReturnTheCart() throws Exception {
        String url = "/cart/view?userId={userId}";
        String userId = "user101";

        Cart cart = Cart.builder()
            .cartId("cart101")
            .build();

        when(cartService.getCartForUser(userId)).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders.get(url, userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.cartId", Is.is("cart101")));
    }

    @Test
    void shouldReturn409WhenCartConflictOccurs() throws Exception {
        GroceryStore store = GroceryStore.builder().outletId("store101").name("Fresh Picks").build();
        Cart existing = Cart.builder()
            .cartId("cart101")
            .catalogType(CatalogType.GROCERY)
            .outlet(store)
            .build();

        when(cartService.addItemToCartForUser(any(AddItemRequest.class)))
            .thenThrow(new CartConflictException(existing, CatalogType.FOOD, "rest201"));

        AddItemRequest req = new AddItemRequest();
        req.setUserId("user101");
        req.setOutletId("rest201");
        req.setCatalogType(CatalogType.FOOD);
        req.setItemId("menu101");
        req.setQuantity(1);
        req.setOverrideExistingCart(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isConflict())
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.overrideRequired", Is.is(true)))
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.existingCatalogType", Is.is("GROCERY")))
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.requestedCatalogType", Is.is("FOOD")));
    }

    @Test
    void shouldReturn200ForAddItem() throws Exception {
        Cart cart = Cart.builder().cartId("cart101").catalogType(CatalogType.GROCERY).build();
        var response = new AddItemResponse(cart, null);

        when(cartService.addItemToCartForUser(any(AddItemRequest.class))).thenReturn(response);

        AddItemRequest req = new AddItemRequest();
        req.setUserId("user101");
        req.setOutletId("store101");
        req.setCatalogType(CatalogType.GROCERY);
        req.setItemId("product101");
        req.setQuantity(1);
        req.setOverrideExistingCart(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.cart.cartId", Is.is("cart101")));
    }
}
