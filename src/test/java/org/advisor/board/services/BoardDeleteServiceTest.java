package org.advisor.board.services;

import org.advisor.board.controllers.RequestBoard;
import org.advisor.board.controllers.RequestConfig;
import org.advisor.board.entities.Board;
import org.advisor.board.repositories.BoardDataRepository;
import org.advisor.board.services.configs.BoardConfigUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
@Transactional
public class BoardDeleteServiceTest {

    @Autowired
    private BoardDeleteService deleteService;

    @Autowired
    private BoardConfigUpdateService configUpdateService;

    @Autowired
    private BoardUpdateService updateService;

    @Autowired
    private BoardDataRepository repository;

    private Board board;
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
        form = new RequestBoard();

        // 조회를 위해 BoardUpdateService로 임의글 작성
        form.setBid(board.getBid());
        form.setMode("write");
        form.setSubject("테스트 제목");
        form.setContent("테스트 내용");
        form.setGid("test");
        form.setPoster("작성자");

        updateService.process(form);

        System.out.println("삭제 전 레포지토리 수 : " + repository.count());

        deleteService.delete(1L);

        System.out.println("삭제 전 레포지토리 후 : " + repository.count());


    }
}
