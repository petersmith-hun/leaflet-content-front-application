package hu.psprog.leaflet.lcfa.core.domain.content;

import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Domain class holding all the necessary information for article page rendering.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class ArticleContent {

    private Article article;
    private List<CategorySummary> categories;
    private List<TagSummary> tags;
    private SEOAttributes seo;
    private List<CommentSummary> comments;
}
