package org.advisor.board.validators;

import lombok.RequiredArgsConstructor;
import org.advisor.board.controllers.RequestBoard;
import org.advisor.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class BoardValidator implements Validator {

    private final MemberUtil memberUtil;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestBoard.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestBoard form = (RequestBoard)target;

        // 수정일때 게시글 번호(seq) 필수 항목
        String mode = form.getMode();
        Long seq = form.getSeq();
        if (mode != null && mode.equals("edit") && (seq == null || seq < 1L)) {
            errors.rejectValue("seq", "NotNull");
        }
    }
}
