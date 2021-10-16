package hu.psprog.leaflet.lcfa.web.interceptor;

import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;
import hu.psprog.leaflet.lcfa.core.domain.common.MenuItem;
import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.EntrySummary;
import hu.psprog.leaflet.lcfa.core.facade.CommonPageDataFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link CommonPageDataInterceptor}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommonPageDataInterceptorTest {

    private static final String PAGE_CONFIG_ATTRIBUTE = "pageConfig";
    private static final List<EntrySummary> ENTRY_SUMMARY_LIST = Collections.singletonList(EntrySummary.builder().title("entry-title").build());
    private static final List<MenuItem> STANDALONE_MENU_ITEMS = Collections.singletonList(MenuItem.builder().routeId("standalone-menu-1").build());
    private static final List<MenuItem> FOOTER_MENU = Collections.singletonList(MenuItem.builder().routeId("footer-menu-1").build());
    private static final List<MenuItem> HEADER_MENU = Collections.singletonList(MenuItem.builder().routeId("header-menu-1").build());
    private static final SEOAttributes SEO_ATTRIBUTES = SEOAttributes.builder().pageTitle("default-seo-title").build();
    private static final CommonPageData COMMON_PAGE_DATA = CommonPageData.builder()
            .latestEntries(ENTRY_SUMMARY_LIST)
            .standaloneMenuItems(STANDALONE_MENU_ITEMS)
            .footerMenu(FOOTER_MENU)
            .headerMenu(HEADER_MENU)
            .seo(SEO_ATTRIBUTES)
            .build();
    private static final String PRE_POPULATED_VALUE = "pre-populated-value";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Mock
    private CommonPageDataFacade commonPageDataFacade;

    @Mock
    private PageConfigModel pageConfigModel;

    @InjectMocks
    private CommonPageDataInterceptor commonPageDataInterceptor;

    @Test
    public void shouldPostHandlePopulateModelAndView() throws Exception {

        // given
        given(request.getDispatcherType()).willReturn(DispatcherType.REQUEST);
        given(commonPageDataFacade.getCommonPageData()).willReturn(COMMON_PAGE_DATA);
        ModelAndView modelAndView = new ModelAndView("test");

        // when
        commonPageDataInterceptor.postHandle(request, response, handler, modelAndView);

        // then
        assertCommonPageData(modelAndView, CommonPageDataField.SEO_ATTRIBUTES, SEO_ATTRIBUTES);
        assertCommonPageData(modelAndView, CommonPageDataField.LATEST_ENTRIES, ENTRY_SUMMARY_LIST);
        assertCommonPageData(modelAndView, CommonPageDataField.STANDALONE_MENU_ITEMS, STANDALONE_MENU_ITEMS);
        assertCommonPageData(modelAndView, CommonPageDataField.HEADER_MENU, HEADER_MENU);
        assertCommonPageData(modelAndView, CommonPageDataField.FOOTER_MENU, FOOTER_MENU);
        assertThat(modelAndView.getModel().get(PAGE_CONFIG_ATTRIBUTE), equalTo(pageConfigModel));
    }

    @Test
    public void shouldPostHandlePopulatePartiallyAlreadyPopulatedModelAndView() throws Exception {

        // given
        given(request.getDispatcherType()).willReturn(DispatcherType.REQUEST);
        given(commonPageDataFacade.getCommonPageData()).willReturn(COMMON_PAGE_DATA);
        ModelAndView modelAndView = new ModelAndView("test");
        modelAndView.addObject("seo", PRE_POPULATED_VALUE);

        // when
        commonPageDataInterceptor.postHandle(request, response, handler, modelAndView);

        // then
        assertCommonPageData(modelAndView, CommonPageDataField.SEO_ATTRIBUTES, PRE_POPULATED_VALUE);
        assertCommonPageData(modelAndView, CommonPageDataField.LATEST_ENTRIES, ENTRY_SUMMARY_LIST);
        assertCommonPageData(modelAndView, CommonPageDataField.STANDALONE_MENU_ITEMS, STANDALONE_MENU_ITEMS);
        assertCommonPageData(modelAndView, CommonPageDataField.HEADER_MENU, HEADER_MENU);
        assertCommonPageData(modelAndView, CommonPageDataField.FOOTER_MENU, FOOTER_MENU);
        assertThat(modelAndView.getModel().get(PAGE_CONFIG_ATTRIBUTE), equalTo(pageConfigModel));
    }

    @Test
    public void shouldIgnorePostHandlingIfModelAndViewIsNull() throws Exception {

        // when
        commonPageDataInterceptor.postHandle(request, response, handler, null);

        // then
        verifyNoInteractions(commonPageDataFacade);
    }

    @Test
    public void shouldIgnorePostHandlingIfDispatcherIsNotRequest() throws Exception {

        // given
        given(request.getDispatcherType()).willReturn(DispatcherType.ERROR);

        // when
        commonPageDataInterceptor.postHandle(request, response, handler, new ModelAndView("test"));

        // then
        verifyNoInteractions(commonPageDataFacade);
    }


    @Test
    public void shouldIgnorePostHandlingIfModelAndViewIsAlreadyPopulated() throws Exception {

        // given
        ModelAndView modelAndView = new ModelAndView("test");
        Arrays.stream(CommonPageDataField.values())
                .forEach(commonPageDataField -> modelAndView.addObject(commonPageDataField.getFieldName(), null));

        // when
        commonPageDataInterceptor.postHandle(request, response, handler, null);

        // then
        verifyNoInteractions(commonPageDataFacade);
    }

    private void assertCommonPageData(ModelAndView modelAndView, CommonPageDataField commonPageDataField, Object value) {
        assertThat(modelAndView.getModel().get(commonPageDataField.getFieldName()), equalTo(value));
    }
}
