package org.advisor.board.services;

import lombok.RequiredArgsConstructor;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.BoardData;
import org.advisor.board.entities.CommentData;
import org.advisor.board.exceptions.BoardNotFoundException;
import org.advisor.board.exceptions.GuestPasswordCheckException;
import org.advisor.board.services.comment.CommentInfoService;
import org.advisor.board.services.configs.BoardConfigInfoService;
import org.advisor.global.exceptions.BadRequestException;
import org.advisor.global.exceptions.UnAuthorizedException;
import org.advisor.global.libs.Utils;
import org.advisor.member.Member;
import org.advisor.member.MemberUtil;
import org.advisor.member.contants.Authority;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardAuthService {
    private final Utils utils;
    private final BoardConfigInfoService configInfoService;
    private final BoardInfoService infoService;
    private final CommentInfoService commentInfoService;
    private final MemberUtil memberUtil;
    /**
     * 게시판 권한 체크
     *
     * @param mode
     * @param bid
     * @param seq
     */
    public void check(String mode, String bid, Long seq) {
        System.out.printf("mode=%s, bid=%s, seq=%d%n", mode, bid, seq);
        if (!StringUtils.hasText(mode) || !StringUtils.hasText(bid) || (List.of("edit", "delete", "comment").contains(mode) && (seq == null || seq < 1L ))) {
            System.out.printf("mode = %s, bid = %s, seq = %s 값 확인 필요%n", mode, bid, seq);
            throw new BadRequestException();
        }

        if (memberUtil.isAdmin()) { // 관리자는 모두 허용
            return;
        }

        Board board = null;
        CommentData comment = null;
        if (mode.equals("comment")) { // 댓글 수정, 삭제
            comment = commentInfoService.get(seq);
            BoardData data = comment.getData();
            board = data.getBoard();
        } else {
            board = configInfoService.get(bid);
        }

        // 게시판 사용 여부 체크
        if (!board.isOpen()) {
            throw new BoardNotFoundException();
        }

        /**
         * mode - write, list  / bid
         *      - edit, view  / seq
         */
        // 글쓰기, 글 목록 권한 체크
        Authority authority = null;
        boolean isVerified = true;
        Member member = memberUtil.getMember(); // 현재 로그인한 회원 정보
        if (List.of("write", "list").contains(mode)) {
            authority = mode.equals("list") ? board.getListAuthority() : board.getWriteAuthority();
        } else if (mode.equals("view")) {
            authority = board.getViewAuthority();

        } else if (List.of("edit", "delete").contains(mode)) { // 수정, 삭제
            /**
             * 1. 회원 게시글인 경우  / 직접 작성한 회원만 수정 가능
             *
             */
            BoardData item = infoService.get(seq);
            String createdBy = item.getCreatedBy();

            if (!memberUtil.isLogin() || !createdBy.equals(member.getEmail())) { // 회원 게시글  - 직접 작성한 회원만 수정 가능 통제 - 미로그인 상태 또는 로그인 상태이지만 작성자의 이메일과 일치하지 않는 경우
                isVerified = false;
            }
        } else if (mode.equals("comment")) { // 댓글 수정 삭제
            String commenter = comment.getCreatedBy();
            if (!memberUtil.isLogin() || !commenter.equals(member.getEmail())) { // 회원이 작성한 댓글
                isVerified = false;
            }
        }

        if ((authority == Authority.USER && !memberUtil.isLogin()) || (authority == Authority.ADMIN && !memberUtil.isAdmin())) { // 회원 권한 + 로그인 X, 관리자 권한 + 관리자 X
            isVerified = false;
        }

        if (!isVerified) {
            throw new UnAuthorizedException();
        }
    }

    public void check(String mode, String bid) {
        check(mode, bid, null);
    }

    public void check(String mode, Long seq) {
        BoardData item = null;
        if (mode.equals("comment")) {
            CommentData comment = commentInfoService.get(seq);
            item = comment.getData();
        } else {
            item = infoService.get(seq);
        }

        Board board = item.getBoard();
        check(mode, board.getBid(), seq);
    }
}
