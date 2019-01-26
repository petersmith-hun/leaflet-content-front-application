package hu.psprog.leaflet.lcfa.web.auth;

import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link RequestAuthentication} implementation required by Bridge Client to authorize requests.
 *
 * @author Peter Smith
 */
@Component
public class JWTTokenBasedRequestAuthentication implements RequestAuthentication {

    private static final String HEADER_PARAMETER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_SCHEMA = "Bearer {0}";

    @Override
    public Map<String, String> getAuthenticationHeader() {

        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Map<String, String> authenticationHeader = new HashMap<>();
        authenticationHeader.put(HEADER_PARAMETER_AUTHORIZATION, MessageFormat.format(AUTHORIZATION_SCHEMA, token));

        return authenticationHeader;
    }
}
