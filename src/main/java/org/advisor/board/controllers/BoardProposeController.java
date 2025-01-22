package org.advisor.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.advisor.board.validators.BoardConfigValidator;
import org.advisor.global.exceptions.BadRequestException;
import org.advisor.global.libs.Utils;
import org.advisor.global.rests.JSONData;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/propose")
public class BoardProposeController {

    private final Utils utils;
    private final BoardConfigValidator boardValidator;

    /**
     * 공통(ALL) 게시글 목록보기 설정
     * @return
     */
    @GetMapping("/list")
    public JSONData list() {

        return null;
    }

    /**
     * 공통(ALL) 게시글 상세보기 설정
     *
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        return null;
    }

    /**
     * 회원(MEMBER) 게시글 등록 처리
     *
     * @param form
     * @param errors
     * @return
     */
    @PostMapping("/save")
    public JSONData save(@Valid @RequestBody RequestBoard form, Errors errors) {
        boardValidator.validate(form, errors);
        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        return null;
    }

    /**
     * 회원 게시글 수정 처리
     *
     * @return
     */
    @GetMapping("/save")
    public JSONData update() {

        return null;
    }

    /**
     * 회원 게시글 삭제 처리
     *
     * @return
     */
    @DeleteMapping("/delete")
    public JSONData M_delete() {

        return null;
    }

    /**
     * 어드민 게시글 삭제 처리
     * @return
     */
    @DeleteMapping("/delete")
    public JSONData A_delete() {

        return null;
    }
}
