package hu.psprog.leaflet.lcfa.core.domain.content;

import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import lombok.Builder;

import java.util.List;

/**
 * Domain class holding all the necessary information for article page rendering.
 *
 * @author Peter Smith
 */
@Builder
public record ArticleContent(
        Article article,
        List<CategorySummary> categories,
        List<TagSummary> tags,
        SEOAttributes seo,
        List<CommentSummary> comments
) { }
