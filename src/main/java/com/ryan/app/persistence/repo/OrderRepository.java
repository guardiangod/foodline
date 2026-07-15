package com.ryan.app.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.ryan.app.persistence.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    List<OrderEntity> findByUser_UserIdOrderByCreatedAtDesc(String userId);

    List<OrderEntity> findAllByOrderByCreatedAtDesc();
}
