package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link UserCommentsPageContentConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserCommentsPageContentConverterTest {

    private static final WrapperBodyDataModel<ExtendedCommentListDataModel> WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(ExtendedCommentListDataModel.getBuilder().build())
            .build();
    private static final List<CommentSummary> COMMENT_SUMMARY_LIST = Collections.singletonList(CommentSummary.builder().id(2L).build());
    private static final PaginationAttributes PAGINATION_ATTRIBUTES = PaginationAttributes.builder().pageNumber(2).build();
    private static final UserCommentsPageContent USER_COMMENTS_PAGE_CONTENT = UserCommentsPageContent.builder()
            .comments(COMMENT_SUMMARY_LIST)
            .paginationAttributes(PAGINATION_ATTRIBUTES)
            .build();

    @Mock
    private CommentSummaryListTransformer commentSummaryListTransformer;

    @Mock
    private WrappedDataExtractor wrappedDataExtractor;

    @InjectMocks
    private UserCommentsPageContentConverter userCommentsPageContentConverter;

    @Test
    public void shouldConvert() {

        // given
        given(commentSummaryListTransformer.convert(WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL.getBody())).willReturn(COMMENT_SUMMARY_LIST);
        given(wrappedDataExtractor.extractPaginationAttributes(WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL)).willReturn(PAGINATION_ATTRIBUTES);

        // when
        UserCommentsPageContent result = userCommentsPageContentConverter.convert(WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(USER_COMMENTS_PAGE_CONTENT));
    }
}
