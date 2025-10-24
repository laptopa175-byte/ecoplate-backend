package com.ecoplate.controller;

import com.ecoplate.model.Order;
import com.ecoplate.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CancelledOrderController.class)
public class CancelledOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private HttpSession session;

    @Test
    public void testCancelOrderWithinTime() throws Exception {
        Long orderId = 1L;
        Long userId = 123L;

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setCreatedAt(LocalDateTime.now().minusMinutes(5));
        order.setStatus("Placed");

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(post("/cancel-order/{orderId}", orderId)
                        .sessionAttr("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Order cancelled successfully"));
    }

    @Test
    public void testCancelOrderAfterTimeLimit() throws Exception {
        Long orderId = 2L;
        Long userId = 123L;

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setCreatedAt(LocalDateTime.now().minusMinutes(15));
        order.setStatus("Placed");

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(post("/cancel-order/{orderId}", orderId)
                        .sessionAttr("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cancellation window expired"));
    }

    @Test
    public void testCancelOrderUnauthorizedUser() throws Exception {
        Long orderId = 3L;
        Long userId = 123L;

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(999L); // Different user
        order.setCreatedAt(LocalDateTime.now().minusMinutes(5));
        order.setStatus("Placed");

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(post("/cancel-order/{orderId}", orderId)
                        .sessionAttr("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Unauthorized cancellation"));
    }
}