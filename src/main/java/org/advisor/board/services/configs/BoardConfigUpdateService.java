package org.advisor.board.services.configs;

import lombok.RequiredArgsConstructor;
import org.advisor.board.controllers.RequestConfig;
import org.advisor.board.entities.Board;
import org.advisor.board.repositories.BoardRepository;
import org.advisor.global.libs.Utils;
import org.advisor.member.contants.Authority;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardConfigUpdateService {
    private final BoardRepository boardRepository;
    private final Utils utils;

    public void process(RequestConfig form) {

        String bid = form.getBid();

        Board board = boardRepository.findById(bid).orElseGet(Board::new);

        board.setBid(bid);
        board.setName(form.getName());
        board.setOpen(form.isOpen());
        board.setCategory(form.getCategory());
        board.setRowsPerPage(form.getRowsPerPage() < 1 ? 20 : form.getRowsPerPage());
        board.setPageRanges(form.getPageRanges() < 1 ? 10 : form.getPageRanges());
        board.setPageRangesMobile(form.getPageRangesMobile() < 1 ? 5 : form.getPageRangesMobile());
        board.setUseEditor(form.isUseEditor());
        board.setUseEditorImage(form.isUseEditorImage());
        board.setUseAttachFile(form.isUseAttachFile());
        board.setListAuthority(Objects.requireNonNullElse(form.getListAuthority(), Authority.ALL));
        board.setViewAuthority(Objects.requireNonNullElse(form.getViewAuthority(), Authority.ALL));
        board.setWriteAuthority(Objects.requireNonNullElse(form.getWriteAuthority(), Authority.ALL));

        String locationAfterWriting = form.getLocationAfterWriting();
        board.setLocationAfterWriting(StringUtils.hasText(locationAfterWriting) ? locationAfterWriting : "list");

        board.setListUnderView(form.isListUnderView());

        boardRepository.saveAndFlush(board);
    }
}