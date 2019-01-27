package hu.psprog.leaflet.lcfa.core.domain.common;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Menu item domain class.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class MenuItem {

    private String routeId;
    private String name;
    private String url;
}
