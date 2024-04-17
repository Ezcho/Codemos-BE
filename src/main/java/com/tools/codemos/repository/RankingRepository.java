package com.tools.codemos.repository;

import com.tools.codemos.model.RankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankingRepository extends JpaRepository<RankingEntity, Long> {
    Optional<RankingEntity> findByEmail(String email);
    void deleteByEmail(String email);
}

