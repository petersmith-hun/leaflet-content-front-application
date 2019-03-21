package hu.psprog.leaflet.lcfa.core.domain.content;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Article details domain class.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Article {

    private Long id;
    private String title;
    private String link;
    private String creationDate;
    private AuthorSummary author;
    private String content;
    private List<TagSummary> tags;
    private List<AttachmentSummary> attachments;
}
