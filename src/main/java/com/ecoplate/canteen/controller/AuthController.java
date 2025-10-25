package com.ecoplate.canteen.controller;

import com.ecoplate.canteen.model.User;
import com.ecoplate.canteen.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // 🔐 Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpSession session) {
        boolean valid = userService.validate(user.getEmail(), user.getPassword());
        if (valid) {
            session.setAttribute("email", user.getEmail());
            session.setAttribute("role", userService.getRole(user.getEmail()));
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // 🆕 Register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        boolean created = userService.register(user);
        return ResponseEntity.ok(created ? "Registered" : "Email already exists");
    }

    // 🧠 Get Role from Session
    @GetMapping("/role")
    public ResponseEntity<String> getRole(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return ResponseEntity.ok(role != null ? role : "UNKNOWN");
    }

    // 🧾 Check Active Session
    @GetMapping("/session")
    public ResponseEntity<?> getSessionUser(HttpSession session) {
        String email = (String) session.getAttribute("email");
        String role = (String) session.getAttribute("role");
        if (email != null && role != null) {
            return ResponseEntity.ok("Session active for " + email + " (" + role + ")");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No active session");
        }
    }

    // 🚪 Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}