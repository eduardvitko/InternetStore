package com.example.InternetStore.services;

import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        return repository.save(user);
    }
    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
