package com.ryan.app.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.app.persistence.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}
