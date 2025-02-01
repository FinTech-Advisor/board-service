package org.advisor.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.advisor.global.entities.BaseEntity;

@Data
public class RequestBoard extends BaseEntity {
    private Long seq; // 게시글 번호
    private String mode;

    @NotBlank
    private String bid; // 게시판 아이디

    @NotBlank
    private String gid;

    @NotBlank
    private String poster; // 작성자

    @NotBlank
    private String subject; // 글 제목

    @NotBlank
    private String content; // 글 내용
    private boolean notice; // 공지글 여부

    private String externalLink; // 외부링크
    private String youtubeUrl; // Youtube 주소

    private String category; // 게시글 분류
}
