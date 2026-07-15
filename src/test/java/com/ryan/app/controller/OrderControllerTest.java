package com.ryan.app.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.ryan.app.domain.CatalogType;
import com.ryan.app.dto.response.OrderResponse;
import com.ryan.app.dto.response.OrderSummaryResponse;
import com.ryan.app.persistence.entity.OrderStatus;
import com.ryan.app.service.OrderService;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldReturn404WhenOrderNotFound() throws Exception {
        when(orderService.getOrderById("ord_x")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/ord_x")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        var resp = OrderResponse.builder()
            .orderId("ord_123")
            .userId("user101")
            .outletId("store101")
            .assignedOutletId("store101")
            .catalogType(CatalogType.GROCERY)
            .status(OrderStatus.CONFIRMED)
            .build();

        when(orderService.getOrderById("ord_123")).thenReturn(resp);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/ord_123")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.orderId", Is.is("ord_123")));
    }

    @Test
    void shouldListOrders() throws Exception {
        when(orderService.listOrders("user101")).thenReturn(List.of(
            OrderSummaryResponse.builder().orderId("ord_1").userId("user101").itemCount(2).build()
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/order?userId=user101")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].orderId", Is.is("ord_1")));
    }
}
