package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.AttachmentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.AuthorSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import hu.psprog.leaflet.lcfa.core.formatter.DateFormatterUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ArticleConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ArticleConverterTest {

    private static final long ENTRY_ID = 1L;
    private static final String USERNAME = "username";
    private static final String RAW_CONTENT = "raw-content";
    private static final ZonedDateTime CREATED = ZonedDateTime.now();
    private static final ZonedDateTime PUBLISHED = ZonedDateTime.now().plusDays(1);
    private static final String LINK = "link";
    private static final String TITLE = "title";
    private static final List<TagDataModel> TAG_DATA_MODEL_LIST = Collections.singletonList(TagDataModel.getBuilder().withId(2L).build());
    private static final List<FileDataModel> FILE_DATA_MODEL_LIST = Collections.singletonList(FileDataModel.getBuilder().withReference("file-ref").build());
    private static final CategoryDataModel CATEGORY_DATA_MODEL = CategoryDataModel.getBuilder().withId(3L).build();
    private static final ExtendedEntryDataModel EXTENDED_ENTRY_DATA_MODEL_WITHOUT_PUBLISHED_DATE = ExtendedEntryDataModel.getBuilder()
            .withId(ENTRY_ID)
            .withUser(UserDataModel.getBuilder()
                    .withUsername(USERNAME)
                    .build())
            .withRawContent(RAW_CONTENT)
            .withCreated(CREATED)
            .withLink(LINK)
            .withTitle(TITLE)
            .withTags(TAG_DATA_MODEL_LIST)
            .withAttachments(FILE_DATA_MODEL_LIST)
            .withCategory(CATEGORY_DATA_MODEL)
            .build();
    private static final ExtendedEntryDataModel EXTENDED_ENTRY_DATA_MODEL_WITH_PUBLISHED_DATE = ExtendedEntryDataModel.getBuilder()
            .withId(ENTRY_ID)
            .withUser(UserDataModel.getBuilder()
                    .withUsername(USERNAME)
                    .build())
            .withRawContent(RAW_CONTENT)
            .withCreated(CREATED)
            .withPublished(PUBLISHED)
            .withLink(LINK)
            .withTitle(TITLE)
            .withTags(TAG_DATA_MODEL_LIST)
            .withAttachments(FILE_DATA_MODEL_LIST)
            .withCategory(CATEGORY_DATA_MODEL)
            .build();
    private static final WrapperBodyDataModel<ExtendedEntryDataModel> SOURCE_OBJECT_WITHOUT_PUBLISHED_DATE = WrapperBodyDataModel.<ExtendedEntryDataModel>getBuilder()
            .withBody(EXTENDED_ENTRY_DATA_MODEL_WITHOUT_PUBLISHED_DATE)
            .build();
    private static final WrapperBodyDataModel<ExtendedEntryDataModel> SOURCE_OBJECT_WITH_PUBLISHED_DATE = WrapperBodyDataModel.<ExtendedEntryDataModel>getBuilder()
            .withBody(EXTENDED_ENTRY_DATA_MODEL_WITH_PUBLISHED_DATE)
            .build();
    private static final List<TagSummary> TAG_SUMMARY_LIST = Collections.singletonList(TagSummary.builder().id(2L).build());
    private static final CategorySummary CATEGORY_SUMMARY = CategorySummary.builder().id(4L).build();
    private static final List<AttachmentSummary> ATTACHMENT_SUMMARY_LIST = Collections.singletonList(AttachmentSummary.builder().link("attachment-link").build());
    private static final String FORMATTED_CREATION_DATE = "creation-date";
    private static final String FORMATTED_PUBLISHED_DATE = "published-date";
    private static final Article EXPECTED_CONVERTED_OBJECT_WITHOUT_PUBLISHED_DATE = Article.builder()
            .id(ENTRY_ID)
            .author(new AuthorSummary(USERNAME))
            .content(RAW_CONTENT)
            .creationDate(FORMATTED_CREATION_DATE)
            .link(LINK)
            .title(TITLE)
            .tags(TAG_SUMMARY_LIST)
            .category(CATEGORY_SUMMARY)
            .attachments(ATTACHMENT_SUMMARY_LIST)
            .build();
    private static final Article EXPECTED_CONVERTED_OBJECT_WITH_PUBLISHED_DATE = Article.builder()
            .id(ENTRY_ID)
            .author(new AuthorSummary(USERNAME))
            .content(RAW_CONTENT)
            .creationDate(FORMATTED_PUBLISHED_DATE)
            .link(LINK)
            .title(TITLE)
            .tags(TAG_SUMMARY_LIST)
            .category(CATEGORY_SUMMARY)
            .attachments(ATTACHMENT_SUMMARY_LIST)
            .build();

    @Mock
    private AttachmentSummaryListConverter attachmentSummaryListConverter;

    @Mock
    private TagSummaryListConverter tagSummaryListConverter;

    @Mock
    private DateFormatterUtility dateFormatterUtility;

    @Mock
    private CategorySummaryConverter categorySummaryConverter;

    @InjectMocks
    private ArticleConverter converter;

    @BeforeEach
    public void setup() {

        // given
        given(tagSummaryListConverter.convert(TAG_DATA_MODEL_LIST)).willReturn(TAG_SUMMARY_LIST);
        given(attachmentSummaryListConverter.convert(FILE_DATA_MODEL_LIST)).willReturn(ATTACHMENT_SUMMARY_LIST);
        given(categorySummaryConverter.convert(CATEGORY_DATA_MODEL)).willReturn(CATEGORY_SUMMARY);
    }

    @Test
    public void shouldConvertWithoutPublishedDate() {

        // given
        given(dateFormatterUtility.formatGeneral(CREATED)).willReturn(FORMATTED_CREATION_DATE);

        // when
        Article result = converter.convert(SOURCE_OBJECT_WITHOUT_PUBLISHED_DATE);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_CONVERTED_OBJECT_WITHOUT_PUBLISHED_DATE));
    }

    @Test
    public void shouldConvertWithPublishedDate() {

        // given
        given(dateFormatterUtility.formatGeneral(PUBLISHED)).willReturn(FORMATTED_PUBLISHED_DATE);

        // when
        Article result = converter.convert(SOURCE_OBJECT_WITH_PUBLISHED_DATE);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_CONVERTED_OBJECT_WITH_PUBLISHED_DATE));
    }
}
