package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Tag summary domain class.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class TagSummary {

    private Long id;
    private String name;
    private String alias;
}
