package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Tag summary domain class.
 *
 * @author Peter Smith
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class TagSummary {

    private Long id;
    private String name;
}
