package org.advisor.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.advisor.board.validators.BoardValidator;
import org.advisor.global.exceptions.BadRequestException;
import org.advisor.global.libs.Utils;
import org.advisor.global.rests.JSONData;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final Utils utils;
    private final BoardValidator boardValidator;

    // 게시판 목록 데이터 처리
    @GetMapping("/list/{bid}")
    public JSONData list(@PathVariable("bid") String bid) {
        commonProcess(bid, "list");



        return null;
    }

    // 게시판 상세보기 데이터 처리
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) { // seq 만 있다면 게시글 번호임.
        commonProcess(seq, "view");

        return null;
    }

    // 게시판 작성 데이터 처리
    @GetMapping("/write/{bid}")
    public JSONData write(@PathVariable("bid") String bid) {

        return null;
    }

    // 게시판 수정 데이터 처리
    @GetMapping("/edit/{seq}")
    public JSONData edit(@PathVariable("seq") Long seq) {
        commonProcess(seq, "edit");

        return null;
    }

    // 게시판 삭제 데이터 처리
    @DeleteMapping("/delete/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq) {
        commonProcess(seq, "delete");

        return null;
    }

    // 게시판 저장 데이터 처리
    @PostMapping("/save")
    public JSONData save(@Valid @RequestBody RequestBoard form, Errors errors) {
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";
        commonProcess(form.getBid(), mode); // 공통 처리 로직 추가

        boardValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        return null;
    }

    /**
     * 게시글 번호로 공통 처리
     *
     * @param seq
     * @param mode
     */
    private void commonProcess(Long seq, String mode) {

    }

    /**
     * 게시판 아이디로 공통 처리
     *
     * @param bid
     * @param mode
     */
    private void commonProcess(String bid, String mode) {

    }


}
