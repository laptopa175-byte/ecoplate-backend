package com.ecoplate.canteen.controller;

import com.ecoplate.canteen.model.Order;
import com.ecoplate.canteen.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/simulate")
    public ResponseEntity<String> simulatePayment(@RequestBody Order order, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }

        order.setUserEmail(email);
        order.setStatus("PAID");
        order.setCreatedAt(LocalDateTime.now());
        orderService.saveOrder(order);

        return ResponseEntity.ok("Payment simulated successfully");
    }

    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory(HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }

        return ResponseEntity.ok(orderService.getOrdersByEmail(email));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }

        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(404).body("Order not found");
        }

        Order order = optionalOrder.get();
        if (!order.getUserEmail().equals(email)) {
            return ResponseEntity.status(403).body("Unauthorized");
        }

        LocalDateTime now = LocalDateTime.now();
        if (order.getCreatedAt().plusMinutes(10).isBefore(now)) {
            return ResponseEntity.status(400).body("Cancellation window expired");
        }

        order.setStatus("CANCELLED");
        orderService.saveOrder(order);
        return ResponseEntity.ok("Order cancelled successfully");
    }
}