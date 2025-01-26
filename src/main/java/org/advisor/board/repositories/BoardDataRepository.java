package org.advisor.board.repositories;

import org.advisor.board.entities.BoardData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface BoardDataRepository extends JpaRepository<BoardData, Long>, QuerydslPredicateExecutor<BoardData> {

    @EntityGraph(attributePaths = "board")
    Optional<BoardData> findBySeq(Long seq);
}
