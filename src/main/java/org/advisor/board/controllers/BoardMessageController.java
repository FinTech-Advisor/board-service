package org.advisor.board.controllers;

import lombok.RequiredArgsConstructor;
import org.advisor.global.rests.JSONData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class BoardMessageController {

    /**
     * 관리자 메세지 목록
     *
     * @param seq
     * @return
     */
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        return null;
    }

    /**
     * 관리자 메세지 상세
     *
     * @return
     */
    @GetMapping("/list")
    public JSONData list() {

        return null;
    }
}
