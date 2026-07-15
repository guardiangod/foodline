package com.ryan.app.service;

import org.springframework.stereotype.Service;

import com.ryan.app.persistence.entity.UserEntity;
import com.ryan.app.persistence.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity fetchUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
