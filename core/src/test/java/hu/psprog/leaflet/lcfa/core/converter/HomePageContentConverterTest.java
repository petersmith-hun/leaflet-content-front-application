package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.EntrySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link HomePageContentConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class HomePageContentConverterTest {

    private static final HomePageRawResponseWrapper POPULATED_HOME_PAGE_RAW_RESPONSE_WRAPPER = HomePageRawResponseWrapper.builder()
            .wrappedEntryListDataModel(WrapperBodyDataModel.<EntryListDataModel>getBuilder().withBody(EntryListDataModel.getBuilder().build()).build())
            .wrappedTagListDataModel(WrapperBodyDataModel.<TagListDataModel>getBuilder().withBody(TagListDataModel.getBuilder().build()).build())
            .categoryListDataModel(CategoryListDataModel.getBuilder().build())
            .build();
    private static final PaginationAttributes PAGINATION_ATTRIBUTES = PaginationAttributes.builder().pageNumber(2).build();
    private static final List<CategorySummary> CATEGORY_SUMMARY_LIST = Collections.singletonList(CategorySummary.builder().id(1L).build());
    private static final List<TagSummary> TAG_SUMMARY_LIST = Collections.singletonList(TagSummary.builder().id(2L).build());
    private static final List<EntrySummary> ENTRY_SUMMARY_LIST = Collections.singletonList(EntrySummary.builder().link("link").build());
    private static final HomePageContent EXPECTED_HOME_PAGE_CONTENT = HomePageContent.builder()
            .categories(CATEGORY_SUMMARY_LIST)
            .tags(TAG_SUMMARY_LIST)
            .entries(ENTRY_SUMMARY_LIST)
            .pagination(PAGINATION_ATTRIBUTES)
            .build();

    @Mock
    private WrappedDataExtractor wrappedDataExtractor;

    @Mock
    private EntrySummaryListConverter entrySummaryListConverter;

    @Mock
    private TagSummaryListConverter tagSummaryListConverter;

    @Mock
    private FilteringDataConversionSupport filteringDataConversionSupport;

    @InjectMocks
    private HomePageContentConverter homePageContentConverter;

    @Test
    public void shouldConvertWithPopulatedSource() {

        // given
        given(filteringDataConversionSupport.mapCategories(POPULATED_HOME_PAGE_RAW_RESPONSE_WRAPPER.categoryListDataModel()))
                .willReturn(CATEGORY_SUMMARY_LIST);
        given(filteringDataConversionSupport.mapOptionalWrapped(POPULATED_HOME_PAGE_RAW_RESPONSE_WRAPPER.wrappedTagListDataModel(), tagSummaryListConverter))
                .willReturn(TAG_SUMMARY_LIST);
        given(entrySummaryListConverter.convert(POPULATED_HOME_PAGE_RAW_RESPONSE_WRAPPER.wrappedEntryListDataModel().body()))
                .willReturn(ENTRY_SUMMARY_LIST);
        given(wrappedDataExtractor.extractPaginationAttributes(POPULATED_HOME_PAGE_RAW_RESPONSE_WRAPPER.wrappedEntryListDataModel()))
                .willReturn(PAGINATION_ATTRIBUTES);

        // when
        HomePageContent result = homePageContentConverter.convert(POPULATED_HOME_PAGE_RAW_RESPONSE_WRAPPER);

        // then
        assertThat(result, equalTo(EXPECTED_HOME_PAGE_CONTENT));
    }

    @Test
    public void shouldSkipConversionAndReturnNullIfWrappedEntryListIsNull() {

        // when
        HomePageContent result = homePageContentConverter.convert(HomePageRawResponseWrapper.builder().build());

        // then
        assertThat(result, nullValue());
    }
}
