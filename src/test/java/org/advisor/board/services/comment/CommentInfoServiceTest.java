package org.advisor.board.services.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.advisor.board.controllers.RequestBoard;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.CommentData;
import org.advisor.board.services.configs.BoardConfigUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
public class CommentInfoServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommentUpdateService commentUpdateService;

    private Board board;
    private RequestBoard form;

    private CommentData comment;
}
