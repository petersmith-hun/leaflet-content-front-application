package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Contact page domain class.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class ContactPageContent {

    private StaticPageContent contactInfo;
}
