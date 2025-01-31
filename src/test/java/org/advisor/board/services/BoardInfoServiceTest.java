package org.advisor.board.services;

<<<<<<< Updated upstream
import org.advisor.board.controllers.RequestBoard;
import org.advisor.board.controllers.RequestConfig;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.BoardData;
import org.advisor.board.services.configs.BoardConfigUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
=======
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
>>>>>>> Stashed changes

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
public class BoardInfoServiceTest {
<<<<<<< Updated upstream
    @Autowired
    private BoardInfoService infoService;

    @Autowired
    private BoardConfigUpdateService configUpdateService;

    private Board board;
    private BoardData boardData;
    private RequestBoard form;

    @BeforeEach
    void init() {
        RequestConfig config = new RequestConfig();
        config.setBid("freetalk");
        config.setName("자유게시판");
        config.setOpen(true);

        board = configUpdateService.process(config);
    }

    @Test
    void test() {
        // BoardUpdateService

    }
=======

>>>>>>> Stashed changes
}
