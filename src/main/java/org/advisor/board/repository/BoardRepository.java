package org.advisor.board.repository;

import org.advisor.board.entities.Board;
import org.advisor.board.entities.QBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardRepository extends JpaRepository<Board, String>, QuerydslPredicateExecutor<Board> {

    default boolean exists(String seq) {
        QBoard board = QBoard.board;

        return exists(board.seq.eq(seq));
    }
}
