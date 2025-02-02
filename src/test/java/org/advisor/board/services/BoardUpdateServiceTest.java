package org.advisor.board.services;

import org.advisor.board.controllers.RequestBoard;
import org.advisor.board.controllers.RequestConfig;
import org.advisor.board.entities.Board;
import org.advisor.board.repositories.BoardDataRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
@Transactional
public class BoardUpdateServiceTest {

    @Autowired
    private BoardUpdateService updateService;

    @Autowired
    private BoardInfoService infoService;

    @Autowired
    private BoardConfigUpdateService configUpdateService;

    @Autowired
    private BoardDataRepository repository;

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

        form = new RequestBoard();

        form.setBid(board.getBid());
        form.setMode("write");
        form.setSubject("테스트 제목");
        form.setContent("테스트 내용");
        form.setGid("test");
        form.setPoster("작성자");

        System.out.println("form : " + form);

        for (int i = 0; i < 5; i++) {
            updateService.process(form);
        }

        System.out.println("데이터의 수 : " + repository.count());
        infoService.get(3L);
        System.out.println(infoService.get(3L));
    }
}
