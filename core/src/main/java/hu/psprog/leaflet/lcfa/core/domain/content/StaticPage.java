package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Static page details domain class.
 *
 * @author Peter Smith
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class StaticPage {

    private String title;
    private String content;
}
