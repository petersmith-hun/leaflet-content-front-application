package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link CommentListDataModel} wrapped as {@link WrapperBodyDataModel} to {@link UserCommentsPageContent} object.
 *
 * @author Peter Smith
 */
@Component
public class UserCommentsPageContentConverter implements Converter<WrapperBodyDataModel<ExtendedCommentListDataModel>, UserCommentsPageContent> {

    private final CommentSummaryListTransformer commentSummaryListTransformer;
    private final WrappedDataExtractor wrappedDataExtractor;

    @Autowired
    public UserCommentsPageContentConverter(CommentSummaryListTransformer commentSummaryListTransformer, WrappedDataExtractor wrappedDataExtractor) {
        this.commentSummaryListTransformer = commentSummaryListTransformer;
        this.wrappedDataExtractor = wrappedDataExtractor;
    }

    @Override
    public UserCommentsPageContent convert(WrapperBodyDataModel<ExtendedCommentListDataModel> source) {
        return UserCommentsPageContent.builder()
                .comments(commentSummaryListTransformer.convert(source.body()))
                .paginationAttributes(wrappedDataExtractor.extractPaginationAttributes(source))
                .build();
    }
}
