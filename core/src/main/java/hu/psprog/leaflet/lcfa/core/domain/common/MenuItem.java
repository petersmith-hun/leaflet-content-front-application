package hu.psprog.leaflet.lcfa.core.domain.common;

import lombok.Builder;

/**
 * Menu item domain class.
 *
 * @author Peter Smith
 */
@Builder
public record MenuItem(
        String routeId,
        String name,
        String url,
        FrontEndRouteAuthRequirement authRequirement
) { }
