package org.advisor.board.controllers;

import lombok.RequiredArgsConstructor;
import org.advisor.global.rests.JSONData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class BoardEventController {

    @GetMapping("/list")
    public JSONData list() {

        return null;
    }

    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

        return null;
    }

    @GetMapping("/save")
    public JSONData update() {

        return null;
    }

    @PostMapping("/save")
    public JSONData save() {

        return null;
    }

    @DeleteMapping("/delete")
    public JSONData delete() {

        return null;
    }
}
