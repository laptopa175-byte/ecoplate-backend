package com.example.canteen.controller;

import com.example.canteen.model.Order;
import com.example.canteen.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void testCancelOrderWithinTime() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", "user@example.com");

        Order order = new Order();
        order.setId(1L);
        order.setUserEmail("user@example.com");
        order.setCreatedAt(LocalDateTime.now().minusMinutes(5));
        order.setStatus("PAID");

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(post("/api/payment/cancel/1").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Order cancelled successfully"));
    }

    @Test
    public void testCancelOrderExpired() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", "user@example.com");

        Order order = new Order();
        order.setId(2L);
        order.setUserEmail("user@example.com");
        order.setCreatedAt(LocalDateTime.now().minusMinutes(15));
        order.setStatus("PAID");

        when(orderService.getOrderById(2L)).thenReturn(Optional.of(order));

        mockMvc.perform(post("/api/payment/cancel/2").session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cancellation window expired"));
    }
}
