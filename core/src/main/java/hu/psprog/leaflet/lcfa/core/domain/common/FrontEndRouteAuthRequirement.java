package hu.psprog.leaflet.lcfa.core.domain.common;

/**
 * Possible front-end route authentication requirements.
 *
 * @author Peter Smith
 */
public enum FrontEndRouteAuthRequirement {

    /**
     * Route should be shown regardless the authentication status.
     */
    SHOW_ALWAYS,

    /**
     * Route should be shown for authenticated users only.
     */
    AUTHENTICATED,

    /**
     * Route should be shown for unauthenticated (anonymous) users only.
     */
    ANONYMOUS
}
