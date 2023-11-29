package com.tools.codemos.repository;

import com.tools.codemos.model.LeaderBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaderBoardRepository extends JpaRepository<LeaderBoardEntity, Integer> {

    @Query(value = "SELECT * FROM leader_board_entity WHERE id >= :start AND id <= :end", nativeQuery = true)
    List<LeaderBoardEntity> findEntriesByRange(@Param("start") int start, @Param("end") int end);
}
