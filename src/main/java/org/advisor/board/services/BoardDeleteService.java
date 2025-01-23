package org.advisor.board.services;

import lombok.RequiredArgsConstructor;
import org.advisor.board.entities.BoardData;
import org.advisor.board.repositories.BoardDataRepository;
import org.advisor.global.libs.Utils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardDeleteService {
    private final BoardInfoService infoService;
    private final BoardDataRepository boardRepository;
    private final RestTemplate restTemplate;
    private final Utils utils;

    public void delete(Long seq) {

        BoardData item = infoService.get(seq);
        String gid = item.getGid();

        // 파일 삭제 S
        String token = utils.getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.hasText(token)) {
            headers.setBearerAuth(token); // 파일 서비스에 토큰값을 보냄
        }

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String apiUrl = utils.serviceUrl("file-service", "/deletes/" + item.getGid());
        restTemplate.exchange(URI.create(apiUrl), HttpMethod.DELETE, request, Void.class);

        // 파일 삭제 E


        boardRepository.delete(item);
        boardRepository.flush();

    }
}
