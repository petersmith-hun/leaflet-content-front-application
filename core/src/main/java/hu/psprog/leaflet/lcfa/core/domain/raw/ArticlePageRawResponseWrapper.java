package hu.psprog.leaflet.lcfa.core.domain.raw;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Wrapper domain class for holding raw responses used by article page rendering.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class ArticlePageRawResponseWrapper {

    private CategoryListDataModel categoryListDataModel;
    private WrapperBodyDataModel<ExtendedEntryDataModel> wrappedExtendedEntryDataModel;
    private WrapperBodyDataModel<TagListDataModel> wrappedTagListDataModel;
    private WrapperBodyDataModel<CommentListDataModel> wrappedCommentListDataModel;
}
