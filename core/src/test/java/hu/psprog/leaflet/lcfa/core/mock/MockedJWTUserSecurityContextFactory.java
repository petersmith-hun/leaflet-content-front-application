package hu.psprog.leaflet.lcfa.core.mock;

import hu.psprog.leaflet.jwt.auth.support.domain.AuthenticationUserDetailsModel;
import hu.psprog.leaflet.jwt.auth.support.domain.JWTTokenAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * Factory to create JWT authenticated based security context mock.
 *
 * @author Peter Smith
 */
public class MockedJWTUserSecurityContextFactory implements WithSecurityContextFactory<WithMockedJWTUser> {

    public static final String TOKEN = "token-for-mock-user";
    public static final String EMAIL_ADDRESS = "user@local.dev";
    private static final String USERNAME = "user1234";

    @Override
    public SecurityContext createSecurityContext(WithMockedJWTUser withMockedJWTUser) {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = JWTTokenAuthentication.getBuilder()
                .withEmailAddress(EMAIL_ADDRESS)
                .withToken(TOKEN)
                .withDetails(prepareUserDetails(withMockedJWTUser))
                .withAuthenticated(withMockedJWTUser.authenticated())
                .build();
        securityContext.setAuthentication(authentication);

        return securityContext;
    }

    private AuthenticationUserDetailsModel prepareUserDetails(WithMockedJWTUser withMockedJWTUser) {
        return AuthenticationUserDetailsModel.getBuilder()
                .withExpiration(createExpirationDate())
                .withId(withMockedJWTUser.userID())
                .withRole(withMockedJWTUser.role())
                .withName(USERNAME)
                .build();
    }

    private Date createExpirationDate() {

        Calendar calendar = new Calendar.Builder()
                .setInstant(new Date())
                .build();
        calendar.add(Calendar.HOUR, 1);

        return calendar.getTime();
    }
}
