package com.ecoplate.canteen.controller;

import com.ecoplate.canteen.model.User;
import com.ecoplate.canteen.model.Order;
import com.ecoplate.canteen.service.UserService;
import com.ecoplate.canteen.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}