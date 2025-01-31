package org.advisor.board.services;

import org.advisor.board.controllers.RequestBoard;
import org.advisor.board.controllers.RequestConfig;
import org.advisor.board.entities.Board;
import org.advisor.board.services.configs.BoardConfigUpdateService;
import org.advisor.member.Member;
import org.advisor.member.MemberUtil;
import org.advisor.member.test.annotations.MockMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
public class BoardUpdateServiceTest {

    @Autowired
    private BoardUpdateService updateService;

    @Autowired
    private BoardConfigUpdateService configUpdateService;

    private RequestConfig config;
    private Board board;
    private RequestBoard form;

    @BeforeEach
    void init() {
        config = new RequestConfig();
        config.setBid("freetalk");
        config.setName("자유게시판");
        config.setOpen(true);


        board = configUpdateService.process(config);
    }

    @Test
    void test() {
        // BoardUpdateService
        System.out.println(board);

        form.setBid(board.getBid());
        form.setMode("write");
        form.setSubject("테스트 제목");
        form.setContent("테스트 내용");
        form.setSeq(1L);
        form.setPoster("작성자");

        System.out.println("form : " + form);

        updateService.process(form);
    }
}
