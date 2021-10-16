package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.domain.common.MenuItem;
import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.EntrySummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CommonPageDataConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommonPageDataConverterTest {

    private static final WrapperBodyDataModel<EntryListDataModel> WRAPPER_BODY_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(EntryListDataModel.getBuilder().build())
            .build();
    private static final SEOAttributes SEO_ATTRIBUTES = SEOAttributes.builder().pageTitle("page-title").build();
    private static final List<MenuItem> HEADER_MENU_ITEM_LIST = Collections.singletonList(MenuItem.builder().routeId("route-1").build());
    private static final List<MenuItem> FOOTER_MENU_ITEM_LIST = Collections.singletonList(MenuItem.builder().routeId("route-2").build());
    private static final List<MenuItem> STANDALONE_MENU_ITEM_LIST = Collections.singletonList(MenuItem.builder().routeId("route-3").build());
    private static final List<EntrySummary> LATEST_ENTRIES = Collections.singletonList(EntrySummary.builder().title("entry").build());
    private static final CommonPageData EXPECTED_COMMON_PAGE_DATA = CommonPageData.builder()
            .seo(SEO_ATTRIBUTES)
            .headerMenu(HEADER_MENU_ITEM_LIST)
            .footerMenu(FOOTER_MENU_ITEM_LIST)
            .standaloneMenuItems(STANDALONE_MENU_ITEM_LIST)
            .latestEntries(LATEST_ENTRIES)
            .build();

    @Mock
    private WrappedDataExtractor wrappedDataExtractor;

    @Mock
    private EntrySummaryListConverter entrySummaryListConverter;

    @InjectMocks
    private CommonPageDataConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(wrappedDataExtractor.extractSEOAttributes(WRAPPER_BODY_DATA_MODEL)).willReturn(SEO_ATTRIBUTES);
        given(wrappedDataExtractor.extractHeaderMenu(WRAPPER_BODY_DATA_MODEL)).willReturn(HEADER_MENU_ITEM_LIST);
        given(wrappedDataExtractor.extractFooterMenu(WRAPPER_BODY_DATA_MODEL)).willReturn(FOOTER_MENU_ITEM_LIST);
        given(wrappedDataExtractor.extractStandaloneMenuItems(WRAPPER_BODY_DATA_MODEL)).willReturn(STANDALONE_MENU_ITEM_LIST);
        given(entrySummaryListConverter.convert(WRAPPER_BODY_DATA_MODEL.getBody())).willReturn(LATEST_ENTRIES);

        // when
        CommonPageData result = converter.convert(WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EXPECTED_COMMON_PAGE_DATA));
    }
}
