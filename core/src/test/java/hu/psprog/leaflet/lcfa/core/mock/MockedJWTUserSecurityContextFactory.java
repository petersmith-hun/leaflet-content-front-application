package hu.psprog.leaflet.lcfa.core.mock;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.Map;

/**
 * Factory to create JWT authenticated based security context mock.
 *
 * @author Peter Smith
 */
public class MockedJWTUserSecurityContextFactory implements WithSecurityContextFactory<WithMockedJWTUser> {

    public static final String EMAIL_ADDRESS = "user@local.dev";

    private static final String USERNAME = "user1234";
    private static final String ATTRIBUTE_SUBJECT = "sub";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_EMAIL = "email";
    private static final String REGISTRATION_ID = "leaflet";

    @Override
    public SecurityContext createSecurityContext(WithMockedJWTUser withMockedJWTUser) {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        OAuth2User oAuth2User = prepareOAuthUser(withMockedJWTUser);
        Authentication authentication = new OAuth2AuthenticationToken(oAuth2User, Collections.emptyList(), REGISTRATION_ID);
        securityContext.setAuthentication(authentication);

        return securityContext;
    }

    private DefaultOAuth2User prepareOAuthUser(WithMockedJWTUser withMockedJWTUser) {
        return new DefaultOAuth2User(Collections.emptyList(), prepareUserAttributes(withMockedJWTUser), ATTRIBUTE_NAME);
    }

    private Map<String, Object> prepareUserAttributes(WithMockedJWTUser withMockedJWTUser) {

        return Map.of(
                ATTRIBUTE_SUBJECT, String.valueOf(withMockedJWTUser.userID()),
                ATTRIBUTE_NAME, USERNAME,
                ATTRIBUTE_EMAIL, EMAIL_ADDRESS
        );
    }
}
