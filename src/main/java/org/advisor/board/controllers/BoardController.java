package org.advisor.board.controllers;

import org.advisor.global.rests.JSONData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class BoardController {

    @GetMapping("/list/{bid}")
    public JSONData eventList() {

        return null;
    }

    @GetMapping("/view/{seq}")
    public JSONData eventView(@PathVariable("seq") Long seq) {

        return null;
    }

    @GetMapping("/list/{bid}")
    public JSONData proposeList() {

        return null;
    }

    @GetMapping("/view/{seq}")
    public JSONData proposeView(@PathVariable("seq") Long seq) {

        return null;
    }



}
