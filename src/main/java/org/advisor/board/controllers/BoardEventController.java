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
@RequestMapping("/event")
public class BoardEventController {

    private final Utils utils;
    private final BoardConfigValidator boardValidator;

    /**
     * 공통(ALL) 이벤트 게시판 목록
     *
     * @return
     */
    @GetMapping("/list")
    public JSONData list() {

        return null;
    }

    /**
     * 공통(ALL) 이벤트 게시판 상세 목록
     *
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        return null;
    }

    /**
     * 관리자 이벤트 게시글 수정
     *
     * @return
     */
    @GetMapping("/save")
    public JSONData update() {


        return null;
    }

    /**
     * 관리자 이벤트 게시글 등록
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
     * 관리자 이벤트 게시글 삭제
     *
     * @return
     */
    @DeleteMapping("/delete")
    public JSONData delete() {

        return null;
    }
}
