package org.advisor.member.test.annotations;

import org.advisor.member.contants.Authority;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface MockAdmin {
    long seq() default 1L;
    String email() default "user02@test.org";
    String name() default "사용자02";
    Authority[] authority() default { Authority.ADMIN };
}
