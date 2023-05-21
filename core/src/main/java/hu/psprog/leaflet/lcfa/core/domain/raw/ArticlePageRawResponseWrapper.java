package hu.psprog.leaflet.lcfa.core.domain.raw;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import lombok.Builder;

/**
 * Wrapper domain class for holding raw responses used by article page rendering.
 *
 * @author Peter Smith
 */
@Builder
public record ArticlePageRawResponseWrapper(
        CategoryListDataModel categoryListDataModel,
        WrapperBodyDataModel<ExtendedEntryDataModel> wrappedExtendedEntryDataModel,
        WrapperBodyDataModel<TagListDataModel> wrappedTagListDataModel,
        WrapperBodyDataModel<CommentListDataModel> wrappedCommentListDataModel
) { }
