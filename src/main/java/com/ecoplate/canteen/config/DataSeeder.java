package com.ecoplate.canteen.config;

import com.example.canteen.model.Order;
import com.example.canteen.model.User;
import com.example.canteen.repository.OrderRepository;
import com.example.canteen.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder {

    @Autowired private UserRepository userRepo;
    @Autowired private OrderRepository orderRepo;

    @PostConstruct
    public void seed() {
        if (userRepo.findByEmail("admin@ecoplate.com") == null) {
            userRepo.save(new User("admin@ecoplate.com", "admin123", "ADMIN"));
            userRepo.save(new User("user@ecoplate.com", "user123", "USER"));
        }

        if (orderRepo.count() == 0) {
            orderRepo.save(new Order("user@ecoplate.com", "Pending"));
            orderRepo.save(new Order("user@ecoplate.com", "Paid"));
        }
    }
}