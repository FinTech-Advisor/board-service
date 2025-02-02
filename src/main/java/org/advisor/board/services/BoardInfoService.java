package org.advisor.board.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.advisor.board.controllers.BoardSearch;
import org.advisor.board.controllers.RequestBoard;
import org.advisor.board.entities.Board;
import org.advisor.board.entities.BoardData;
import org.advisor.board.entities.QBoardData;
import org.advisor.board.exceptions.BoardDataNotFoundException;
import org.advisor.board.repositories.BoardDataRepository;
import org.advisor.board.services.configs.BoardConfigInfoService;
import org.advisor.global.libs.Utils;
import org.advisor.global.paging.ListData;
import org.advisor.global.paging.Pagination;
import org.advisor.member.Member;
import org.advisor.member.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardInfoService {

    private final BoardConfigInfoService configInfoService;
    private final BoardDataRepository boardDataRepository;
    private final JPAQueryFactory queryFactory;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;
    private final ModelMapper modelMapper;
    private final Utils utils;

    /**
     * 게시글 한개 조회
     *
     * @param seq
     * @return
     */
    public BoardData get(Long seq) {
        System.out.println("************************get 입장************************");
        System.out.println("seq : " + seq);
        System.out.println(boardDataRepository.count());
        BoardData item = boardDataRepository.findBySeq(seq).orElseThrow(BoardDataNotFoundException::new);

        addInfo(item, true); // 추가 정보 처리

        return item;
    }

    public RequestBoard getForm(Long seq) {
        return getForm(get(seq));
    }

    /**
     * 수정 처리시 커맨드 객체 RequestBoard로 변환
     *
     * @param item
     * @return
     */
    public RequestBoard getForm(BoardData item) {
        RequestBoard form = modelMapper.map(item, RequestBoard.class);
        form.setMode("edit");
        form.setBid(item.getBoard().getBid());

        return form;
    }

    /**
     * 게시글 목록
     *
     * @param search
     * @return
     */
    public ListData<BoardData> getList(BoardSearch search) {
        int page = Math.max(search.getPage(), 1);
        Board board = null;
        int rowsPerPage = 0;
        List<String> bids = search.getBid();
        if (bids != null && !bids.isEmpty()) {
            board = configInfoService.get(bids.get(0));
            rowsPerPage = board.getRowsPerPage();
        }
        int limit = search.getLimit() > 0 ? search.getLimit() : rowsPerPage;
        int offset = (page - 1) * limit;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();
        QBoardData boardData = QBoardData.boardData;

        // 게시판 아이디
        if (bids != null && !bids.isEmpty()) {
            andBuilder.and(boardData.board.bid.in(bids));
        }

        // 분류 검색
        List<String> categories = search.getCategory();
        if (categories != null && !categories.isEmpty()) {
            andBuilder.and(boardData.category.in(categories));
        }

        /**
         * 키워드 검색
         *  sopt
         *      - ALL - 제목 + 내용 + 작성자(작성자 + 이메일 + 회원명)
         *      - SUBJECT - 제목
         *      - CONTENT - 내용
         *      - SUBJECT_CONTENT - 제목 + 내용
         *      - POSTER - 작성자 + 이메일 + 회원명
         */
        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            StringExpression subject = boardData.subject;
            StringExpression content = boardData.content;
            StringExpression poster = boardData.poster.concat(boardData.createdBy);


            StringExpression condition = null;
            if (sopt.equals("SUBJECT")) { // 제목 검색
                condition = subject;
            } else if (sopt.equals("CONTENT")) { // 내용 검색
                condition = content;
            } else if (sopt.equals("SUBJECT_CONTENT")) { // 제목 + 내용
                condition = subject.concat(content);
            } else if (sopt.equals("POSTER")){
                condition = poster;
            } else { // 통합 검색
                condition = subject.concat(content).concat(poster);
            }

            andBuilder.and(condition.contains(skey));
        }

        // 회원 이메일
        List<String> emails = search.getEmail();
        if (emails != null && !emails.isEmpty()) {
            andBuilder.and(boardData.createdBy.in(emails));
        }

        /* 검색 처리 E */

        JPAQuery<BoardData> query = queryFactory.selectFrom(boardData)
                .leftJoin(boardData.board)
                .fetchJoin()
                .where(andBuilder)
                .offset(offset)
                .limit(limit);

        List<BoardData> items = query.fetch();

        long total = boardDataRepository.count(andBuilder);

        items.forEach(this::addInfo); // 추가 정보 처리

        int ranges = utils.isMobile() ? 5 : 10;
        if (board != null) { // 게시판별 설정이 있는 경우
            ranges = utils.isMobile() ? board.getPageRangesMobile() : board.getPageRanges();
        }

        Pagination pagination = new Pagination(page, (int)total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }

    public ListData<BoardData> getList(String bid, BoardSearch search) {
        search.setBid(List.of(bid));

        return getList(search);
    }

    /**
     * 게시판별 최신 게시글
     *
     * @param bid
     * @param limit
     * @return
     */
    public List<BoardData> getLatest(String bid, String category, int limit) {
        BoardSearch search = new BoardSearch();
        search.setLimit(limit);
        search.setBid(List.of(bid));
        search.setCategory(category == null ? null : List.of(category));

        ListData<BoardData> data = getList(search);

        List<BoardData> items = data.getItems();

        System.out.println(items.size());

        return items == null ? List.of() : items;
    }

    public List<BoardData> getLatest(String bid, int limit) {
        return getLatest(bid, null, limit);
    }

    public List<BoardData> getLatest(String bid) {
        return getLatest(bid, 5);
    }

    /**
     * 로그인한 회원이 작성한 게시글 목록
     *
     * @param search
     * @return
     */
    public ListData<BoardData> getMyList(BoardSearch search) {
        if (!memberUtil.isLogin()) {
            return new ListData<>(List.of(), null);
        }

        Member member = memberUtil.getMember();
        String email = member.getEmail();
        search.setEmail(List.of(email));

        return getList(search);
    }

    /**
     * 추가 정보 처리
     *
     * @param item
     */
    private void addInfo(BoardData item, boolean isView) {

        // 이전, 다음 게시글
        if (isView) { // 보기 페이지 데이터를 조회하는 경우만 이전, 다음 게시글을 조회
            QBoardData boardData = QBoardData.boardData;
            Long seq = item.getSeq();

            BoardData prev = queryFactory.selectFrom(boardData)
                    .where(boardData.seq.lt(seq))
                    .orderBy(boardData.seq.desc())
                    .fetchFirst();

            BoardData next = queryFactory.selectFrom(boardData)
                            .where(boardData.seq.gt(seq))
                            .orderBy(boardData.seq.asc())
                            .fetchFirst();

            item.setPrev(prev);
            System.out.println("prev의 값 : " + item.getPrev());
            item.setNext(next);
            System.out.println("next의 값 : " + item.getNext());
        }

        /* listable, writable, editable, mine 처리 S */

        Board board = item.getBoard();
        configInfoService.addInfo(board);

        boolean listable = board.isListable();

        boolean writable = board.isWritable();

        String createdBy = item.getCreatedBy();
        Member loggedMember = memberUtil.getMember();

        boolean editable = memberUtil.isLogin() && loggedMember.getEmail().equals(createdBy); // 회원게시글 로그인 한 회원과 일치하면 버튼 노출

        boolean mine = utils.getValue(utils.getUserHash() + "_board_" + item.getSeq()) != null
                        || (memberUtil.isLogin() && loggedMember.getEmail().equals(createdBy));

        item.setListable(listable);
        item.setWritable(writable);
        item.setEditable(editable);
        item.setMine(mine);

        /* listable, writable, editable, mine 처리 E */
    }

    private void addInfo(BoardData item) {
        addInfo(item, false);
    }

    /**
     * 게시글 번호와 게시판 아이디로 현재 페이지 구하기
     *
     * @param seq
     * @param limit
     * @return
     */
    public int getPage(String bid, Long seq, int limit) {
        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(boardData.board.bid.eq(bid))
                .and(boardData.seq.goe(seq));

        long total = boardDataRepository.count(builder);
        int page = (int)Math.ceil((double)total / limit);

        return page;
    }
}
