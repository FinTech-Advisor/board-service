package org.advisor.board.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.BoardData;
import org.advisor.board.services.*;
import org.advisor.board.services.configs.BoardConfigInfoService;
import org.advisor.board.validators.BoardValidator;
import org.advisor.global.exceptions.BadRequestException;
import org.advisor.global.libs.Utils;
import org.advisor.global.paging.ListData;
import org.advisor.global.rests.JSONData;
import org.advisor.member.MemberUtil;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final Utils utils;
    private final BoardValidator boardValidator;
    private final BoardConfigInfoService configInfoService;
    private final ViewUpdateService viewupdateService;
    private final BoardUpdateService updateService;
    private final BoardInfoService infoService;
    private final BoardDeleteService deleteService;
    private final BoardAuthService authService;
    private final MemberUtil memberUtil;

    /**
     * 게시판 설정 한개 조회
     *
     * @param bid
     * @return
     */
    @Operation(summary = "게시판 설정 조회")
    @ApiResponse(responseCode = "201", description = "게시판 설정 조회, 게시판 설정 반환")
    @Parameters({
            @Parameter(name="bid", description = "게시판 그룹 ID", required = true),
    })
    @GetMapping("/config/{bid}")
    public JSONData config(@PathVariable("bid") String bid) {
        Board board = configInfoService.get(bid);

        return new JSONData(board);
    }

    /**
     * 게시글 등록, 수정 처리
     *
     * @return
     */
    @Operation(summary = "게시글 등록 및 수정 처리")
    @ApiResponse(responseCode = "201", description = "게시판 등록 및 수정 처리")
    @Parameters({
            @Parameter(name="글 번호", description = "게시글 번호", required = true),
            @Parameter(name="글 모드", description = "write(등록) 혹은 edit(수정)"),
            @Parameter(name="글 제목", description = "글 제목", required = true),
            @Parameter(name="글 내용", description = "글 내용", required = true),
    })
    @PostMapping("/save")
    public JSONData save(@Valid @RequestBody RequestBoard form, Errors errors) {
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";
        commonProcess(form.getBid(), mode); // 공통 처리

        boardValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        BoardData data = updateService.process(form);

        return new JSONData(data);

    }
    /**
     * 게시글 한개 조회
     * - 글보기, 글 수정시에 활용될 수 있음(프론트앤드)
     *
     * @param seq
     * @return
     */
    @Operation(summary = "게시판 설정 조회")
    @ApiResponse(responseCode = "201", description = "게시판 설정 조회, 게시판 설정 반환")
    @Parameters({
            @Parameter(name="bid", description = "게시판 그룹 ID", required = true),
    })
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq, Errors errors) {
        commonProcess(seq, "view");

        BoardData data = infoService.get(seq);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        return new JSONData(data);
    }

    /**
     * 게시글 목록 조회
     *
     * @param bid
     * @return
     */
    @Operation(summary = "게시글 목록 조회")
    @ApiResponse(responseCode = "201", description = "게시판 목록 조회  시 ")
    @Parameters({
            @Parameter(name="seq", description = "게시글 번호", required = true),
            @Parameter(name="mode", description = "write(등록) 혹은 edit(수정) 무엇이냐에 따라 저장 처리를 달리함"),
    })
    @GetMapping("/list/{bid}")
    public JSONData list(@PathVariable("bid") String bid, @ModelAttribute BoardSearch search) {
        commonProcess(bid, "list");

        ListData<BoardData> data = infoService.getList(bid, search);

        return new JSONData(data);
    }

    /**
     * 게시글 단일 혹은 복수 삭제
     *
     * @param seqs
     * @return
     */
    @DeleteMapping("/deletes")
    public JSONData delete(@RequestParam("seq") List<Long> seqs) {
        commonProcess(seqs, "delete");
        List<BoardData> item = deleteService.delete(seqs);

        return new JSONData(item);
    }

    /**
     * 조회수 업데이트 처리
     *
     * @param seq
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/viewcount/{seq}")
    public JSONData updateViewCount(@PathVariable("seq") Long seq) {
        long viewCount = viewupdateService.process(seq);

        return new JSONData(viewCount);
    }


    /**
     * 게시글 번호로 공통 처리(seq가 1개일 때)
     *
     * @param seq
     * @param mode
     */
    private void commonProcess(Long seq, String mode) {
        authService.check(mode, seq); // 게시판 권한 체크 - 조회, 삭제
    }

    /**
     * 게시글 번호로 공통 처리(seq가 2개 이상일 때)
     *
     * @param seqs
     * @param mode
     */
    private void commonProcess(List<Long> seqs, String mode) {

        authService.check(seqs, mode); // 게시판 권한 체크 - 조회, 삭제
    }

    /**
     * 게시판 아이디로 공통 처리
     *
     * @param bid
     * @param mode
     */
    private void commonProcess(String bid, String mode) {

        // 권한 체크
        if (!List.of("edit", "delete", "comment").contains(mode)) {
            authService.check(mode, bid); // 게시판 권한 체크 - 글 목록, 글 작성
        }

        Board board = configInfoService.get(bid);
        String pageTitle = board.getName();

    }
}
