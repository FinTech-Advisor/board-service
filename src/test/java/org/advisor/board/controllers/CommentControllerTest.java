package org.advisor.board.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.BoardData;
import org.advisor.board.entities.CommentData;
import org.advisor.board.services.configs.BoardConfigUpdateService;
import org.advisor.global.rests.JSONData;
import org.advisor.member.test.annotations.MockMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BoardConfigUpdateService configUpdateService;

    private Board board;
    private RequestBoard form;

    private CommentData comment;

    @BeforeEach
    @MockMember
    @DisplayName("게시글 작성 테스트")
    void init() {
        RequestConfig config = new RequestConfig();
        config.setBid("freetalk");
        config.setName("자유게시판");
        config.setOpen(true);
        config.setUseComment(true);

        board = configUpdateService.process(config);
        form = new RequestBoard();
        form.setBid(board.getBid());
        form.setSubject("제목");
        form.setPoster("작성자");
        form.setContent("내용");
        form.setGid(UUID.randomUUID().toString());
    }

    @Test
    @MockMember
    @DisplayName("게시글 답변 테스트")
    void commentTest() throws Exception {
        String body = om.writeValueAsString(form);

        // 게시글 등록
        String res = mockMvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONData jsonData = om.readValue(res, JSONData.class);
        BoardData data = om.readValue(om.writeValueAsString(jsonData.getData()), BoardData.class);

    }
}
