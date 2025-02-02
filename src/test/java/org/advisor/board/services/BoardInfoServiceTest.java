package org.advisor.board.services;

import org.advisor.board.controllers.BoardSearch;
import org.advisor.board.controllers.RequestBoard;
import org.advisor.board.controllers.RequestConfig;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.BoardData;
import org.advisor.board.repositories.BoardDataRepository;
import org.advisor.board.services.configs.BoardConfigInfoService;
import org.advisor.board.services.configs.BoardConfigUpdateService;
import org.advisor.member.Member;
import org.advisor.member.contants.Authority;
import org.advisor.member.test.annotations.MockMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
@Transactional
public class BoardInfoServiceTest {
    @Autowired
    private BoardUpdateService updateService;

    @Autowired
    private BoardInfoService infoService;

    @Autowired
    private BoardConfigUpdateService configUpdateService;

    @Autowired
    private BoardDataRepository repository;

    private Member member;

    private Board board;
    private RequestBoard form;
    private BoardSearch boardSearch;

    @BeforeEach
    void init() {
        RequestConfig config = new RequestConfig();
        config.setBid("freetalk");
        config.setName("자유게시판");
        config.setOpen(true);

        board = configUpdateService.process(config);
    }

    @Test
    @MockMember
    void test() {
        form = new RequestBoard();

        // 조회를 위해 BoardUpdateService로 임의글 작성
        form.setBid(board.getBid());
        form.setMode("write");
        form.setSubject("테스트 제목");
        form.setContent("테스트 내용");
        form.setGid("test");
        form.setPoster("작성자");

        for (int i = 0; i < 10; i++) {
            updateService.process(form);
        }

        System.out.println("BoardDataRepository : " + repository);

        System.out.println("findBySeq() : " + repository.findBySeq(3L));

        // BoardInfoService - get
        System.out.println("---------------------------------------");
        infoService.get(3L);
        System.out.println("---------------------------------------");

        // BoardInfoService - gerList
        boardSearch = new BoardSearch();

        boardSearch.setBid(List.of("freetalk"));

        infoService.getList(boardSearch);

        // BoardInfoService - getLatest
        System.out.println("****************getLatest****************");
        infoService.getLatest("freetalk");
        System.out.println(repository.count());
        System.out.println("****************getLatest****************");

        // 특정 멤버가 쓴 게시글 - BoardUpdateService
        System.out.println(board);

        member = new Member();
        member.setSeq(5L);
        member.setEmail("testMember@gmail.com");
        member.setName("테스트멤버");
        member.set_authorities(List.of(Authority.USER));

        form.setBid(board.getBid());
        form.setMode("write");
        form.setSubject("테스트 제목");
        form.setContent("테스트 내용");
        form.setGid("test");
        form.setPoster(member.getName());

        for (int i = 0; i < 10; i++) {
            updateService.process(form);
        }

        boardSearch.setEmail(List.of(member.getEmail()));

        System.out.println("****************내가 쓴 글****************");
        infoService.getMyList(boardSearch);
        System.out.println("****************내가 쓴 글****************");
    }
}
