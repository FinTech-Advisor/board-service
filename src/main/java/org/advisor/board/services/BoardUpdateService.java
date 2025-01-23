package org.advisor.board.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.advisor.board.controllers.RequestBoard;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.BoardData;
import org.advisor.board.exceptions.BoardDataNotFoundException;
import org.advisor.board.repositories.BoardDataRepository;
import org.advisor.board.services.configs.BoardConfigInfoService;
import org.advisor.global.libs.Utils;
import org.advisor.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@Lazy
@Service
@Transactional
@RequiredArgsConstructor
public class BoardUpdateService {

    private final BoardConfigInfoService configInfoService;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;
    private final RestTemplate restTemplate;
    private final Utils utils;

    public BoardData process(RequestBoard form) {

        Long seq = Objects.requireNonNullElse(form.getSeq(), 0L);
        String mode = Objects.requireNonNullElse(form.getMode(), "write");

        BoardData data = null;
        if (mode.equals("edit")) { // 수정
            data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);
        } else { // 추가
            /**
             * 등록될때만 최초 한번 기록되는 데이터
             * - 게시판 설정, 회원
             * - gid
             * - 아이피, UserAgent
             */
            Board board = configInfoService.get(form.getBid());
            data = new BoardData();
            data.setBoard(board);
            data.setGid(form.getGid());
            data.setIpAddr(request.getRemoteAddr());
            data.setUserAgent(request.getHeader("User-Agent"));
        }

        data.setPoster(form.getPoster());

        // 공지글 여부는 관리자만 반영 가능
        if (memberUtil.isAdmin()) {
            data.setNotice(form.isNotice());
        }

        data.setSubject(form.getSubject());
        data.setContent(form.getContent());
        data.setExternalLink(form.getExternalLink());
        data.setYoutubeUrl(form.getYoutubeUrl());
        data.setCategory(form.getCategory());

        boardDataRepository.saveAndFlush(data);

        /* 게시글 파일 첨부 작업 완료 처리 S */
        String apiUrl = utils.serviceUrl("file-service", "/done/" + data.getGid());
        HttpEntity<Void> request = new HttpEntity<>(utils.getRequestHeader());
        restTemplate.exchange(URI.create(apiUrl), HttpMethod.GET, request, Void.class);
        /* 게시글 파일 첨부 작업 완료 처리 E */

        // 비회원 게시글 인증 정보 삭제
        utils.deleteValue(utils.getUserHash() + "_board_" + seq);

        return data;
    }
}
