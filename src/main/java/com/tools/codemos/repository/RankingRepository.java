package com.tools.codemos.repository;

import com.tools.codemos.model.RankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RankingRepository extends JpaRepository<RankingEntity, Long> {
    Optional<RankingEntity> findByLoginId(String loginId);
    void deleteByLoginId(String loginId);
}

