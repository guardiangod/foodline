package com.ryan.app.persistence.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.app.persistence.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, String> {

    Optional<CartEntity> findByUser_UserId(String userId);
}
