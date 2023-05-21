package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;

/**
 * Attachment summary domain class.
 *
 * @author Peter Smith
 */
@Builder
public record AttachmentSummary(
        String name,
        String link,
        AttachmentType type,
        String description
) { }
