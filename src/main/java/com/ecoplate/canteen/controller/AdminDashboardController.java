package com.ecoplate.controller;

import com.ecoplate.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("ADMIN")) {
            return ResponseEntity.status(403).body("Access denied");
        }

        long totalOrders = orderRepository.count();
        long cancelledOrders = orderRepository.countByStatus("Cancelled");
        long completedOrders = orderRepository.countByStatus("Completed");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("cancelledOrders", cancelledOrders);
        stats.put("completedOrders", completedOrders);

        return ResponseEntity.ok(stats);
    }
}