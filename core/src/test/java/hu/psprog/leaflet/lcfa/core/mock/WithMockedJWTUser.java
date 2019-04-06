package hu.psprog.leaflet.lcfa.core.mock;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Mocked JWT user.
 *
 * @author Peter Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockedJWTUserSecurityContextFactory.class)
public @interface WithMockedJWTUser {

    long userID() default 1L;
    String role() default "ADMIN";
    boolean authenticated() default true;
}
