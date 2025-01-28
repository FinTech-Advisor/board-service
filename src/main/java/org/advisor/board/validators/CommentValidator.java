package org.advisor.board.validators;

import lombok.RequiredArgsConstructor;
import org.advisor.board.controllers.RequestComment;
import org.advisor.board.repositories.CommentDataRepository;
import org.advisor.global.validators.PasswordValidator;
import org.advisor.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class CommentValidator implements Validator, PasswordValidator {

    private final MemberUtil memberUtil;
    private final PasswordEncoder passwordEncoder;
    private final CommentDataRepository commentDataRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestComment.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        /**
         * 1. 수정모드인 경우 seq 필수
         */
        RequestComment form = (RequestComment) target;
        String mode = form.getMode();
        Long seq = form.getSeq();

        // 1. 수정모드인 경우 seq 필수
        if (mode != null && mode.equals("edit") && (seq == null || seq < 1L)) {
            errors.rejectValue("seq", "NotNull");
        }
    }
}
