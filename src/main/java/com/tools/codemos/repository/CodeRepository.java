package com.tools.codemos.repository;

import com.tools.codemos.model.CodeEntity;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<CodeEntity, Integer> {
    @Query("SELECT c FROM CodeEntity c WHERE c.leaderBoard.user.id = :userId")
    List<CodeEntity> findByUserId(@Param("userId") Long userId);
}
