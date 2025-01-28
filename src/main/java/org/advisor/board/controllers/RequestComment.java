package org.advisor.board.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestComment {
    private String mode;
    private Long seq;

    @NotNull
    private Long boardDataSeq;

    @NotBlank
    private String commenter;

    @NotBlank
    private String content;
}
