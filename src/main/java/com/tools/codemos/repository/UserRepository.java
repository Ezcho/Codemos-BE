package com.tools.codemos.repository;

import com.tools.codemos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}