package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.converter.Converter;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for {@link FilteringDataConversionSupport}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FilteringDataConversionSupportTest {

    private static final CategoryListDataModel CATEGORY_LIST_DATA_MODEL = CategoryListDataModel.getBuilder().build();
    private static final List<CategorySummary> CATEGORY_SUMMARY_LIST = Collections.singletonList(CategorySummary.builder().build());
    private static final CommentListDataModel COMMENT_LIST_DATA_MODEL = CommentListDataModel.getBuilder().withComments(Collections.emptyList()).build();
    private static final WrapperBodyDataModel<CommentListDataModel> WRAPPED_COMMENT_LIST_DATA_MODEL = WrapperBodyDataModel.getBuilder().withBody(COMMENT_LIST_DATA_MODEL).build();
    private static final ExtendedEntryDataModel EXTENDED_ENTRY_DATA_MODEL = ExtendedEntryDataModel.getExtendedBuilder().build();
    private static final List<CommentSummary> COMMENT_SUMMARY_LIST = Collections.singletonList(CommentSummary.builder().build());
    private static final List<String> EXPECTED_RESULT_FOR_OPTIONAL_WRAPPED = Collections.singletonList("test");
    private static final Converter<CommentListDataModel, List<String>> TEST_CONVERTER = commentListDataModel -> EXPECTED_RESULT_FOR_OPTIONAL_WRAPPED;

    @Mock
    private CommentSummaryListTransformer commentSummaryListTransformer;

    @Mock
    private CategorySummaryListConverter categorySummaryListConverter;

    @InjectMocks
    private FilteringDataConversionSupport filteringDataConversionSupport;

    @Test
    public void shouldMapCategoriesWithPopulatedInput() {

        // given
        given(categorySummaryListConverter.convert(CATEGORY_LIST_DATA_MODEL)).willReturn(CATEGORY_SUMMARY_LIST);

        // when
        List<CategorySummary> result = filteringDataConversionSupport.mapCategories(CATEGORY_LIST_DATA_MODEL);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        assertThat(result, equalTo(CATEGORY_SUMMARY_LIST));
        verify(categorySummaryListConverter).convert(CATEGORY_LIST_DATA_MODEL);
    }

    @Test
    public void shouldMapCategoriesWithNullInput() {

        // when
        List<CategorySummary> result = filteringDataConversionSupport.mapCategories(null);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasSize(0));
        verifyZeroInteractions(categorySummaryListConverter);
    }

    @Test
    public void shouldMapCommentsWithPopulatedInput() {

        // given
        given(commentSummaryListTransformer.convert(COMMENT_LIST_DATA_MODEL.getComments(), EXTENDED_ENTRY_DATA_MODEL)).willReturn(COMMENT_SUMMARY_LIST);

        // when
        List<CommentSummary> result = filteringDataConversionSupport.mapComments(WRAPPED_COMMENT_LIST_DATA_MODEL, EXTENDED_ENTRY_DATA_MODEL);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        assertThat(result, equalTo(COMMENT_SUMMARY_LIST));
        verify(commentSummaryListTransformer).convert(COMMENT_LIST_DATA_MODEL.getComments(), EXTENDED_ENTRY_DATA_MODEL);
    }

    @Test
    public void shouldMapCommentsWithNullInput() {

        // when
        List<CommentSummary> result = filteringDataConversionSupport.mapComments(null, EXTENDED_ENTRY_DATA_MODEL);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasSize(0));
        verifyZeroInteractions(commentSummaryListTransformer);
    }

    @Test
    public void shouldMapOptionalWrappedWithPopulatedInput() {

        // when
        List<String> result = filteringDataConversionSupport.mapOptionalWrapped(WRAPPED_COMMENT_LIST_DATA_MODEL, TEST_CONVERTER);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_RESULT_FOR_OPTIONAL_WRAPPED));
    }

    @Test
    public void shouldMapOptionalWrappedWithNullInput() {

        // when
        List<String> result = filteringDataConversionSupport.mapOptionalWrapped(null, TEST_CONVERTER);

        // then
        assertThat(result, notNullValue());
        assertThat(result, hasSize(0));
    }
}
