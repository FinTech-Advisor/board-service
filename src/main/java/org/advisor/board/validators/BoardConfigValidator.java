package org.advisor.board.validators;

import org.advisor.board.controllers.RequestConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Configuration
public class BoardConfigValidator implements Validator {
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
        mode = StringUtils.hasText(mode) ? mode : "add";
        String seq = form.getSeq();

    }
}
