package com.example.canteen.service;

import com.example.canteen.model.User;
import com.example.canteen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepo;

    public boolean validate(String email, String password) {
        User user = userRepo.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    public boolean register(User user) {
        if (userRepo.findByEmail(user.getEmail()) != null) return false;
        userRepo.save(user);
        return true;
    }

    public String getRole(String email) {
        User user = userRepo.findByEmail(email);
        return user != null ? user.getRole() : null;
    }
}