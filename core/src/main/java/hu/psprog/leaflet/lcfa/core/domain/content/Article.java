package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;

import java.util.List;

/**
 * Article details domain class.
 *
 * @author Peter Smith
 */
@Builder
public record Article(
        Long id,
        String title,
        String link,
        String creationDate,
        AuthorSummary author,
        String content,
        List<TagSummary> tags,
        List<AttachmentSummary> attachments,
        CategorySummary category
) { }
