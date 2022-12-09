package com.example.demo.repository;

import com.example.demo.domain.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    Optional<PostEntity> findByUsername(String username);
}
