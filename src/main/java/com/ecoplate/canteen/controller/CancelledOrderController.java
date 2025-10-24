package com.ecoplate.controller;

import com.ecoplate.model.Order;
import com.ecoplate.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/cancel-order")
public class CancelledOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(404).body("Order not found");
        }

        Order order = optionalOrder.get();
        if (!order.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body("Unauthorized cancellation");
        }

        Duration duration = Duration.between(order.getCreatedAt(), LocalDateTime.now());
        if (duration.toMinutes() > 10) {
            return ResponseEntity.badRequest().body("Cancellation window expired");
        }

        order.setStatus("Cancelled");
        orderRepository.save(order);
        return ResponseEntity.ok("Order cancelled successfully");
    }
}
