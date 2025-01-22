package org.advisor.board.controllers;

import lombok.RequiredArgsConstructor;
import org.advisor.global.rests.JSONData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    @GetMapping("view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        return null;
    }

    @GetMapping("list")
    public JSONData list() {

        return null;
    }

    @PostMapping
    public JSONData message() {

        return null;
    }
}
