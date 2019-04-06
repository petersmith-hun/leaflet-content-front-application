package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.MenuDataModel;
import hu.psprog.leaflet.api.rest.response.common.PaginationDataModel;
import hu.psprog.leaflet.api.rest.response.common.SEODataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.routing.FrontEndRouteDataModel;
import hu.psprog.leaflet.lcfa.core.domain.common.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.lcfa.core.domain.common.MenuItem;
import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link WrappedDataExtractor}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class WrappedDataExtractorTest {

    private static final boolean HAS_NEXT = true;
    private static final boolean HAS_PREVIOUS = true;
    private static final int PAGE_COUNT = 3;
    private static final int PAGE_NUMBER = 2;
    private static final String PAGE_TITLE = "page title";
    private static final String META_TITLE = "meta title";
    private static final String META_DESCRIPTION = "meta description";
    private static final String META_KEYWORDS = "meta keywords";
    private static final String HEADER_ROUTE_NAME = "header route name";
    private static final String HEADER_ROUTE_ID = "header route ID";
    private static final String HEADER_ROUTE_URL = "header route url";
    private static final String HEADER_AUTH_REQUIREMENT = "ANONYMOUS";
    private static final String FOOTER_ROUTE_NAME = "footer route name";
    private static final String FOOTER_ROUTE_ID = "footer route ID";
    private static final String FOOTER_ROUTE_URL = "footer route url";
    private static final String FOOTER_AUTH_REQUIREMENT = "AUTHENTICATED";
    private static final String STANDALONE_ROUTE_NAME = "standalone route name";
    private static final String STANDALONE_ROUTE_ID = "standalone route ID";
    private static final String STANDALONE_ROUTE_URL = "standalone route url";
    private static final String STANDALONE_AUTH_REQUIREMENT = "SHOW_ALWAYS";
    private static final WrapperBodyDataModel<?> WRAPPER_BODY_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withPagination(PaginationDataModel.getBuilder()
                    .withHasNext(HAS_NEXT)
                    .withHasPrevious(HAS_PREVIOUS)
                    .withPageCount(PAGE_COUNT)
                    .withPageNumber(PAGE_NUMBER)
                    .build())
            .withSeo(SEODataModel.getBuilder()
                    .withPageTitle(PAGE_TITLE)
                    .withMetaTitle(META_TITLE)
                    .withMetaDescription(META_DESCRIPTION)
                    .withMetaKeywords(META_KEYWORDS)
                    .build())
            .withMenu(MenuDataModel.getBuilder()
                    .withHeader(Collections.singletonList(FrontEndRouteDataModel.getBuilder()
                            .withName(HEADER_ROUTE_NAME)
                            .withRouteId(HEADER_ROUTE_ID)
                            .withUrl(HEADER_ROUTE_URL)
                            .withAuthRequirement(HEADER_AUTH_REQUIREMENT)
                            .build()))
                    .withFooter(Collections.singletonList(FrontEndRouteDataModel.getBuilder()
                            .withName(FOOTER_ROUTE_NAME)
                            .withRouteId(FOOTER_ROUTE_ID)
                            .withUrl(FOOTER_ROUTE_URL)
                            .withAuthRequirement(FOOTER_AUTH_REQUIREMENT)
                            .build()))
                    .withStandalone(Collections.singletonList(FrontEndRouteDataModel.getBuilder()
                            .withName(STANDALONE_ROUTE_NAME)
                            .withRouteId(STANDALONE_ROUTE_ID)
                            .withUrl(STANDALONE_ROUTE_URL)
                            .withAuthRequirement(STANDALONE_AUTH_REQUIREMENT)
                            .build()))
                    .build())
            .build();
    private static final WrapperBodyDataModel<?> EMPTY_WRAPPER_BODY_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withMenu(MenuDataModel.getBuilder().build())
            .build();
    private static final PaginationAttributes EXPECTED_PAGINATION_ATTRIBUTES = PaginationAttributes.builder()
            .pageNumber(PAGE_NUMBER)
            .pageCount(PAGE_COUNT)
            .hasNext(HAS_NEXT)
            .hasPrevious(HAS_PREVIOUS)
            .build();
    private static final PaginationAttributes DEFAULT_PAGINATION_ATTRIBUTES = PaginationAttributes.builder()
            .pageNumber(1)
            .pageCount(1)
            .hasPrevious(false)
            .hasNext(false)
            .build();
    private static final SEOAttributes EXPECTED_SEO_ATTRIBUTES = SEOAttributes.builder()
            .pageTitle(PAGE_TITLE)
            .metaTitle(META_TITLE)
            .metaDescription(META_DESCRIPTION)
            .metaKeywords(META_KEYWORDS)
            .build();
    private static final SEOAttributes DEFAULT_SEO_ATTRIBUTES = SEOAttributes.builder().build();
    private static final MenuItem HEADER_MENU_ITEM = MenuItem.builder()
            .name(HEADER_ROUTE_NAME)
            .routeId(HEADER_ROUTE_ID)
            .url(HEADER_ROUTE_URL)
            .authRequirement(FrontEndRouteAuthRequirement.valueOf(HEADER_AUTH_REQUIREMENT))
            .build();
    private static final MenuItem FOOTER_MENU_ITEM = MenuItem.builder()
            .name(FOOTER_ROUTE_NAME)
            .routeId(FOOTER_ROUTE_ID)
            .url(FOOTER_ROUTE_URL)
            .authRequirement(FrontEndRouteAuthRequirement.valueOf(FOOTER_AUTH_REQUIREMENT))
            .build();
    private static final MenuItem STANDALONE_MENU_ITEM = MenuItem.builder()
            .name(STANDALONE_ROUTE_NAME)
            .routeId(STANDALONE_ROUTE_ID)
            .url(STANDALONE_ROUTE_URL)
            .authRequirement(FrontEndRouteAuthRequirement.valueOf(STANDALONE_AUTH_REQUIREMENT))
            .build();


    @InjectMocks
    private WrappedDataExtractor wrappedDataExtractor;

    @Test
    public void shouldExtractPaginationAttributesFromPopulatedSource() {

        // when
        PaginationAttributes result = wrappedDataExtractor.extractPaginationAttributes(WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, equalTo(EXPECTED_PAGINATION_ATTRIBUTES));
    }

    @Test
    public void shouldExtractPaginationAttributesFromEmptySource() {

        // when
        PaginationAttributes result = wrappedDataExtractor.extractPaginationAttributes(EMPTY_WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, equalTo(DEFAULT_PAGINATION_ATTRIBUTES));
    }

    @Test
    public void shouldExtractSEOAttributesFromPopulatedSource() {

        // when
        SEOAttributes result = wrappedDataExtractor.extractSEOAttributes(WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, equalTo(EXPECTED_SEO_ATTRIBUTES));
    }

    @Test
    public void shouldExtractSEOAttributesFromEmptySource() {

        // when
        SEOAttributes result = wrappedDataExtractor.extractSEOAttributes(EMPTY_WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, equalTo(DEFAULT_SEO_ATTRIBUTES));
    }
    
    @Test
    public void shouldExtractHeaderMenuFromPopulatedSource() {
        
        // when
        List<MenuItem> result = wrappedDataExtractor.extractHeaderMenu(WRAPPER_BODY_DATA_MODEL);
        
        // then
        assertThat(result, equalTo(Collections.singletonList(HEADER_MENU_ITEM)));
    }

    @Test
    public void shouldExtractHeaderMenuFromEmptySource() {

        // when
        List<MenuItem> result = wrappedDataExtractor.extractHeaderMenu(EMPTY_WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldExtractFooterMenuFromPopulatedSource() {

        // when
        List<MenuItem> result = wrappedDataExtractor.extractFooterMenu(WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, equalTo(Collections.singletonList(FOOTER_MENU_ITEM)));
    }

    @Test
    public void shouldExtractFooterMenuFromEmptySource() {

        // when
        List<MenuItem> result = wrappedDataExtractor.extractFooterMenu(EMPTY_WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, equalTo(Collections.emptyList()));
    }

    @Test
    public void shouldExtractStandaloneMenuFromPopulatedSource() {

        // when
        List<MenuItem> result = wrappedDataExtractor.extractStandaloneMenuItems(WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, equalTo(Collections.singletonList(STANDALONE_MENU_ITEM)));
    }

    @Test
    public void shouldExtractStandaloneMenuFromEmptySource() {

        // when
        List<MenuItem> result = wrappedDataExtractor.extractStandaloneMenuItems(EMPTY_WRAPPER_BODY_DATA_MODEL);

        // then
        assertThat(result, equalTo(Collections.emptyList()));
    }
}
