package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
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
 * Unit tests for {@link ArticleContentConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class ArticleContentConverterTest {

    private static final ArticlePageRawResponseWrapper SOURCE_OBJECT = ArticlePageRawResponseWrapper.builder()
            .wrappedExtendedEntryDataModel(WrapperBodyDataModel.<ExtendedEntryDataModel>getBuilder()
                    .withBody(ExtendedEntryDataModel.getExtendedBuilder().withId(1L).build())
                    .build())
            .categoryListDataModel(CategoryListDataModel.getBuilder().withItem(CategoryDataModel.getBuilder().withID(4L).build()).build())
            .wrappedTagListDataModel(WrapperBodyDataModel.<TagListDataModel>getBuilder()
                    .withBody(TagListDataModel.getBuilder().withItem(TagDataModel.getBuilder().withId(2L).build()).build())
                    .build())
            .wrappedCommentListDataModel(WrapperBodyDataModel.<CommentListDataModel>getBuilder()
                    .withBody(CommentListDataModel.getBuilder().withItem(CommentDataModel.getBuilder().withId(3L).build()).build())
                    .build())
            .build();
    private static final Article ARTICLE = Article.builder().id(1L).build();
    private static final List<CategorySummary> CATEGORY_SUMMARY_LIST = Collections.singletonList(CategorySummary.builder().id(4L).build());
    private static final List<TagSummary> TAG_SUMMARY_LIST = Collections.singletonList(TagSummary.builder().id(2L).build());
    private static final List<CommentSummary> COMMENT_SUMMARY_LIST = Collections.singletonList(CommentSummary.builder().id(3L).build());
    private static final SEOAttributes SEO_ATTRIBUTES = SEOAttributes.builder().pageTitle("page-title").build();
    private static final ArticleContent EXPECTED_CONVERTED_OBJECT = ArticleContent.builder()
            .article(ARTICLE)
            .categories(CATEGORY_SUMMARY_LIST)
            .tags(TAG_SUMMARY_LIST)
            .comments(COMMENT_SUMMARY_LIST)
            .seo(SEO_ATTRIBUTES)
            .build();

    @Mock
    private ArticleConverter articleConverter;

    @Mock
    private TagSummaryListConverter tagSummaryListConverter;

    @Mock
    private WrappedDataExtractor wrappedDataExtractor;

    @Mock
    private FilteringDataConversionSupport filteringDataConversionSupport;

    @InjectMocks
    private ArticleContentConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(articleConverter.convert(SOURCE_OBJECT.getWrappedExtendedEntryDataModel())).willReturn(ARTICLE);
        given(filteringDataConversionSupport.mapCategories(SOURCE_OBJECT.getCategoryListDataModel())).willReturn(CATEGORY_SUMMARY_LIST);
        given(filteringDataConversionSupport.mapOptionalWrapped(SOURCE_OBJECT.getWrappedTagListDataModel(), tagSummaryListConverter)).willReturn(TAG_SUMMARY_LIST);
        given(wrappedDataExtractor.extractSEOAttributes(SOURCE_OBJECT.getWrappedExtendedEntryDataModel())).willReturn(SEO_ATTRIBUTES);
        given(filteringDataConversionSupport.mapComments(SOURCE_OBJECT.getWrappedCommentListDataModel(), SOURCE_OBJECT.getWrappedExtendedEntryDataModel().getBody())).willReturn(COMMENT_SUMMARY_LIST);

        // when
        ArticleContent result = converter.convert(SOURCE_OBJECT);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_CONVERTED_OBJECT));
    }
}
