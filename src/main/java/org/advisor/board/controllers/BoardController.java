package org.advisor.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.BoardData;
import org.advisor.board.services.*;
import org.advisor.board.services.configs.BoardConfigInfoService;
import org.advisor.board.validators.BoardValidator;
import org.advisor.global.exceptions.BadRequestException;
import org.advisor.global.libs.Utils;
import org.advisor.global.paging.ListData;
import org.advisor.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final Utils utils;
    private final BoardValidator boardValidator;
    private final BoardConfigInfoService configInfoService;
    private final BoardUpdateService updateService;
    private final BoardInfoService infoService;
    private final BoardDeleteService deleteService;
    private final BoardAuthService authService;

    /**
     * 게시판 설정 한개 조회
     *
     * @param bid
     * @return
     */
    @GetMapping("/config/{bid}")
    public JSONData config(@PathVariable("bid") String bid) {
        Board board = configInfoService.get(bid);

        return new JSONData(board);
    }

    /**
     * 게시글 등록, 수정 처리
     *
     * @return
     */
    @PostMapping("/save")
    public JSONData save(@Valid @RequestBody RequestBoard form, Errors errors) {
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";
        commonProcess(form.getBid(), mode); // 공통 처리

        boardValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        BoardData data = updateService.process(form);

        return new JSONData(data);
    }

    /**
     * 게시글 한개 조회
     * - 글보기, 글 수정시에 활용될 수 있음(프론트앤드)
     *
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {
        commonProcess(seq, "view");

        BoardData data = infoService.get(seq);

        return new JSONData(data);
    }

    /**
     * 게시글 목록 조회
     *
     * @param bid
     * @return
     */
    @GetMapping("/list/{bid}")
    public JSONData list(@PathVariable("bid") String bid, @ModelAttribute BoardSearch search) {
        commonProcess(bid, "list");

        ListData<BoardData> data = infoService.getList(bid, search);

        return new JSONData(data);
    }

    /**
     * 게시글 한개 삭제
     *
     * @param seq
     * @return
     */
    @DeleteMapping("/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq) {
        commonProcess(seq, "delete");

        boardValidator.checkDelete(seq); // 댓글이 존재하면 삭제 불가
        BoardData item = deleteService.delete(seq);

        return new JSONData(item);
    }

    @GetMapping
    public JSONData mine() {
        return null;
    }

    /**
     * 게시글 번호로 공통 처리
     *
     * @param seq
     * @param mode
     */
    private void commonProcess(Long seq, String mode) {
        authService.check(mode, seq); // 게시판 권한 체크 - 조회, 수정, 삭제
    }

    /**
     * 게시판 아이디로 공통 처리
     *
     * @param bid
     * @param mode
     */
    private void commonProcess(String bid, String mode) {

        // 권한 체크
        if (!List.of("edit", "delete", "comment").contains(mode)) {
            authService.check(mode, bid); // 게시판 권한 체크 - 글 목록, 글 작성
        }

        Board board = configInfoService.get(bid);
        String pageTitle = board.getName();

    }
}
