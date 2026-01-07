package com.ryan.app.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.ryan.app.domain.User;
import com.ryan.app.seedData.SeedData;

@Service
public class UserService {

    private final List<User> users= SeedData.users;

    public User fetchUserById(String userId) {
        return users.stream()
            .filter(user -> userId.equals(user.getUserId()))
            .findFirst()
            .orElse(null);
    }

}
