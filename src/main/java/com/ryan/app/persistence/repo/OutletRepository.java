package com.ryan.app.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.app.persistence.entity.OutletEntity;

public interface OutletRepository extends JpaRepository<OutletEntity, String> {
}
