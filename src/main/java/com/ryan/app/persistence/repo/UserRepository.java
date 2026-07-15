package com.ryan.app.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.app.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
