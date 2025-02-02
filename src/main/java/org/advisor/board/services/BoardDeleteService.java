package org.advisor.board.services;

import lombok.RequiredArgsConstructor;
import org.advisor.board.entities.BoardData;
import org.advisor.board.repositories.BoardDataRepository;
import org.advisor.global.libs.Utils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardDeleteService {
    private final BoardInfoService infoService;
    private final BoardDataRepository boardRepository;
    private final RestTemplate restTemplate;
    private final Utils utils;

    public BoardData delete(Long seq) {

        BoardData item = infoService.get(seq);

        // 파일 삭제 S

        /*
        HttpEntity<Void> request = new HttpEntity<>(utils.getRequestHeader());
        String apiUrl = utils.serviceUrl("file-service", "/delete" + item.getGid());
        restTemplate.exchange(URI.create(apiUrl), HttpMethod.GET, request, Void.class);
        */

        // 파일 삭제 E

        boardRepository.delete(item);
        boardRepository.saveAndFlush(item);


        return item;
    }

    public List<BoardData> delete(List<Long> seqs) {

        List<BoardData> data = new ArrayList<>();

        for  (Long seq : seqs) {
            BoardData item = delete(seq);

            if (item != null) {
                data.add(item);
            }
        }

        return data;
    }
}
