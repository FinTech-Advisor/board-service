package org.advisor.board.repositories;

import org.advisor.board.entities.BoardView;
import org.advisor.board.entities.BoardViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardViewRepository extends JpaRepository<BoardView, BoardViewId>, QuerydslPredicateExecutor<BoardView> {
}
