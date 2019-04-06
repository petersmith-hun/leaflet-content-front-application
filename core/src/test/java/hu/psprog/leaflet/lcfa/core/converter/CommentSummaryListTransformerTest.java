package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.AuthorSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentArticleData;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.formatter.DateFormatterUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CommentSummaryListTransformer}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentSummaryListTransformerTest {

    private static final long COMMENT_ID_5 = 5L;
    private static final long COMMENT_ID_6 = 6L;
    private static final long USER_ID_1 = 1L;
    private static final long USER_ID_2 = 2L;
    private static final String USERNAME_1 = "username-1";
    private static final String USERNAME_2 = "username-2";
    private static final String COMMENT_CONTENT_ID_5 = "comment-content-id-5";
    private static final String COMMENT_CONTENT_ID_6 = "comment-content-id-6";
    private static final boolean COMMENT_5_ENABLED_FLAG = true;
    private static final boolean COMMENT_5_DELETED_FLAG = false;
    private static final boolean COMMENT_6_ENABLED_FLAG = false;
    private static final boolean COMMENT_6_DELETED_FLAG = true;
    private static final ZonedDateTime CREATED = ZonedDateTime.now();
    private static final String FORMATTED_CREATED_DATE = "formatted-created-date";
    private static final String ENTRY_TITLE = "entry-title";
    private static final String ENTRY_LINK = "entry-link";
    private static final CommentDataModel COMMENT_DATA_MODEL_USER_1 = CommentDataModel.getBuilder()
            .withId(COMMENT_ID_5)
            .withOwner(UserDataModel.getBuilder()
                    .withId(USER_ID_1)
                    .withUsername(USERNAME_1)
                    .build())
            .withContent(COMMENT_CONTENT_ID_5)
            .withCreated(CREATED)
            .withEnabled(COMMENT_5_ENABLED_FLAG)
            .withDeleted(COMMENT_5_DELETED_FLAG)
            .build();
    private static final CommentDataModel COMMENT_DATA_MODEL_USER_2 = CommentDataModel.getBuilder()
            .withId(COMMENT_ID_6)
            .withOwner(UserDataModel.getBuilder()
                    .withId(USER_ID_2)
                    .withUsername(USERNAME_2)
                    .build())
            .withContent(COMMENT_CONTENT_ID_6)
            .withCreated(CREATED)
            .withEnabled(COMMENT_6_ENABLED_FLAG)
            .withDeleted(COMMENT_6_DELETED_FLAG)
            .build();
    private static final EntryDataModel ASSOCIATED_ENTRY_DATA_MODEL = EntryDataModel.getBuilder()
            .withTitle(ENTRY_TITLE)
            .withLink(ENTRY_LINK)
            .build();
    private static final EntryDataModel EXTERNAL_ENTRY_DATA_MODEL = EntryDataModel.getBuilder()
            .withUser(UserDataModel.getBuilder()
                    .withId(USER_ID_1)
                    .build())
            .build();
    private static final ExtendedCommentDataModel EXTENDED_COMMENT_DATA_MODEL = ExtendedCommentDataModel.getExtendedBuilder()
            .withId(COMMENT_ID_5)
            .withOwner(UserDataModel.getBuilder()
                    .withId(USER_ID_1)
                    .withUsername(USERNAME_1)
                    .build())
            .withContent(COMMENT_CONTENT_ID_5)
            .withCreated(CREATED)
            .withEnabled(COMMENT_5_ENABLED_FLAG)
            .withDeleted(COMMENT_5_DELETED_FLAG)
            .withAssociatedEntry(ASSOCIATED_ENTRY_DATA_MODEL)
            .build();
    private static final CommentSummary COMMENT_SUMMARY_5_NO_AUTHOR_CHECK = CommentSummary.builder()
            .id(COMMENT_ID_5)
            .author(new AuthorSummary(USERNAME_1))
            .content(COMMENT_CONTENT_ID_5)
            .enabled(COMMENT_5_ENABLED_FLAG)
            .deleted(COMMENT_5_DELETED_FLAG)
            .createdByArticleAuthor(false)
            .created(FORMATTED_CREATED_DATE)
            .build();
    private static final CommentSummary COMMENT_SUMMARY_5_AUTHOR_CHECK = CommentSummary.builder()
            .id(COMMENT_ID_5)
            .author(new AuthorSummary(USERNAME_1))
            .content(COMMENT_CONTENT_ID_5)
            .enabled(COMMENT_5_ENABLED_FLAG)
            .deleted(COMMENT_5_DELETED_FLAG)
            .createdByArticleAuthor(true)
            .created(FORMATTED_CREATED_DATE)
            .build();
    private static final CommentSummary COMMENT_SUMMARY_6 = CommentSummary.builder()
            .id(COMMENT_ID_6)
            .author(new AuthorSummary(USERNAME_2))
            .content(COMMENT_CONTENT_ID_6)
            .enabled(COMMENT_6_ENABLED_FLAG)
            .deleted(COMMENT_6_DELETED_FLAG)
            .createdByArticleAuthor(false)
            .created(FORMATTED_CREATED_DATE)
            .build();
    private static final CommentSummary COMMENT_SUMMARY_EXTENDED = CommentSummary.builder()
            .id(COMMENT_ID_5)
            .author(new AuthorSummary(USERNAME_1))
            .content(COMMENT_CONTENT_ID_5)
            .enabled(COMMENT_5_ENABLED_FLAG)
            .deleted(COMMENT_5_DELETED_FLAG)
            .createdByArticleAuthor(false)
            .created(FORMATTED_CREATED_DATE)
            .article(new CommentArticleData(ENTRY_TITLE, ENTRY_LINK))
            .build();

    @Mock
    private DateFormatterUtility dateFormatterUtility;

    @InjectMocks
    private CommentSummaryListTransformer transformer;

    @Test
    public void shouldConvertCommentListData() {

        // given
        given(dateFormatterUtility.formatComments(CREATED)).willReturn(FORMATTED_CREATED_DATE);
        CommentListDataModel commentListDataModel = CommentListDataModel.getBuilder()
                .withItem(COMMENT_DATA_MODEL_USER_1)
                .withItem(COMMENT_DATA_MODEL_USER_2)
                .build();

        // when
        List<CommentSummary> result = transformer.convert(commentListDataModel);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasItem(COMMENT_SUMMARY_5_NO_AUTHOR_CHECK));
        assertThat(result, hasItem(COMMENT_SUMMARY_6));
    }

    @Test
    public void shouldConvertExtendedCommentListData() {

        // given
        given(dateFormatterUtility.formatComments(CREATED)).willReturn(FORMATTED_CREATED_DATE);
        ExtendedCommentListDataModel commentListDataModel = ExtendedCommentListDataModel.getBuilder()
                .withItem(EXTENDED_COMMENT_DATA_MODEL)
                .build();

        // when
        List<CommentSummary> result = transformer.convert(commentListDataModel);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasItem(COMMENT_SUMMARY_EXTENDED));
    }

    @Test
    public void shouldConvertCommentListDataWithAuthorCheck() {

        // given
        given(dateFormatterUtility.formatComments(CREATED)).willReturn(FORMATTED_CREATED_DATE);
        List<CommentDataModel> commentDataModelList = Arrays.asList(COMMENT_DATA_MODEL_USER_1, COMMENT_DATA_MODEL_USER_2);

        // when
        List<CommentSummary> result = transformer.convert(commentDataModelList, EXTERNAL_ENTRY_DATA_MODEL);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasItem(COMMENT_SUMMARY_5_AUTHOR_CHECK));
        assertThat(result, hasItem(COMMENT_SUMMARY_6));
    }
}
