package org.advisor.board.services;

import org.advisor.board.controllers.RequestBoard;
import org.advisor.board.controllers.RequestConfig;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.BoardData;
import org.advisor.board.services.configs.BoardConfigUpdateService;
import org.advisor.member.Member;
import org.advisor.member.MemberUtil;
import org.advisor.member.contants.Authority;
import org.advisor.member.test.annotations.MockMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
@Transactional
public class BoardAuthServiceTest {
    @Autowired
    private BoardAuthService authService;

    @Autowired
    private BoardUpdateService updateService;

    @Autowired
    private BoardConfigUpdateService configUpdateService;

    private Board board;
    private RequestConfig requestConfig;
    private Member member;
    private RequestBoard form;

    @BeforeEach
    void init() {
        requestConfig = new RequestConfig();
        requestConfig.setBid("freetalk");
        requestConfig.setName("자유게시판");
        requestConfig.setOpen(true);
    }

    @Test
    @MockMember(seq = 1L, name = "사용자01", email = "user@test.org", authority = Authority.USER)
    void test() {

        board = configUpdateService.process(requestConfig);

        // member = new Member();

        /*System.out.println("Member 선언 전");
        member.setSeq(1L);
        member.setEmail("user01@test.org");
        member.setName("사용자01");
        // member.set_authorities(List.of(Authority.ADMIN));
        member.set_authorities(List.of(Authority.USER));
        System.out.println("Member 선언 후");*/


        // authService.check("write", "freetalk", 1L);
        System.out.println("--------------------------------------------------------------------");
        authService.check("write", "freetalk");
        System.out.println("--------------------------------------------------------------------");

        authService.check("edit", "freetalk", 1L);

    }
}
