package hu.psprog.leaflet.lcfa.web.thymeleaf.support.security;

import hu.psprog.leaflet.lcfa.core.domain.common.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.lcfa.core.domain.common.MenuItem;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Security context evaluator for showing front-end routes with specific authentication requirements.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteAuthRequirementEvaluator {

    /**
     * Decides whether the given {@link MenuItem} object can be displayed on the UI.
     * A front-end route can be shown in either of the cases below:
     *  - menu item does not specify any special authentication requirement (item will always be displayed)
     *  - menu item requires authenticated state and security context contains {@link OAuth2AuthenticationToken} in authenticated status
     *  - menu item requires anonymous state and security context contains {@link AnonymousAuthenticationToken}
     * Any different combinations than the ones above will cause the item to remain hidden.
     *
     * @param menuItem {@link MenuItem} object to be checked if it can be displayed
     * @return display status as boolean - {@code true} if the item can be displayed, {@code false} otherwise
     */
    public boolean canDisplay(MenuItem menuItem) {
        return isAuthenticationIndependent(menuItem)
                || isAuthenticated(menuItem)
                || isAnonymous(menuItem);
    }

    private boolean isAuthenticationIndependent(MenuItem menuItem) {
        return menuItem.authRequirement() == FrontEndRouteAuthRequirement.SHOW_ALWAYS;
    }

    private boolean isAuthenticated(MenuItem menuItem) {
        return menuItem.authRequirement() == FrontEndRouteAuthRequirement.AUTHENTICATED
                && getAuthentication() instanceof OAuth2AuthenticationToken
                && getAuthentication().isAuthenticated();
    }

    private boolean isAnonymous(MenuItem menuItem) {
        return menuItem.authRequirement() == FrontEndRouteAuthRequirement.ANONYMOUS
                && getAuthentication() instanceof AnonymousAuthenticationToken;
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
