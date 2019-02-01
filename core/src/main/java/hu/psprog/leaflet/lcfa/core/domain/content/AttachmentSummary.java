package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Attachment summary domain class.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class AttachmentSummary {

    private String name;
    private String link;
    private AttachmentType type;
    private String description;
}
