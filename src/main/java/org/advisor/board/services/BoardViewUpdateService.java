package org.advisor.board.services;

import lombok.RequiredArgsConstructor;
import org.advisor.board.entities.BoardData;
import org.advisor.board.entities.BoardView;
import org.advisor.board.entities.QBoardView;
import org.advisor.board.repositories.BoardDataRepository;
import org.advisor.board.repositories.BoardViewRepository;
import org.advisor.global.libs.Utils;
import org.advisor.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardViewUpdateService {
    private final BoardDataRepository boardDataRepository;
    private final BoardViewRepository boardViewRepository;
    private final MemberUtil memberUtil;
    private final Utils utils;

    public long process(Long seq) {
        BoardData item = boardDataRepository.findById(seq).orElse(null);
        if (item == null) return 0L;

        try {
            BoardView view = new BoardView();
            view.setSeq(seq);
            view.setHash(utils.getMemberHash());
            boardViewRepository.saveAndFlush(view);
        } catch (Exception e) {}

        // 조회수 업데이트
        QBoardView boardView = QBoardView.boardView;
        long total = boardViewRepository.count(boardView.seq.eq(seq));

        item.setViewCount(total);
        boardDataRepository.saveAndFlush(item);

        return total;
    }
}
