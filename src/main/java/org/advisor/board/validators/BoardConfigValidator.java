package org.advisor.board.validators;

import lombok.RequiredArgsConstructor;
import org.advisor.board.controllers.RequestConfig;
import org.advisor.board.repositories.BoardRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class BoardConfigValidator implements Validator {

    private final BoardRepository boardRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestConfig.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        RequestConfig form = (RequestConfig)target;
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";

        String bid = form.getBid();
        if (mode.equals("write") && boardRepository.exists(bid)) {
            // 게시판 아이디의 중복 여부 체크
            errors.rejectValue("bid", "Duplicated");
        }

    }
}
