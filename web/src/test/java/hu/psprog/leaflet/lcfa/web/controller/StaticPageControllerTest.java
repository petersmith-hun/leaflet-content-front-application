package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPage;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;
import hu.psprog.leaflet.lcfa.core.facade.StaticPageContentFacade;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl.NavigationItemFactoryRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link StaticPageController}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(MockitoExtension.class),
        @ExtendWith(SpringExtension.class)
})
public class StaticPageControllerTest extends AbstractControllerTest {

    private static final SEOAttributes SEO_ATTRIBUTES = SEOAttributes.builder().pageTitle("intro-page-seo-title").build();
    private static final StaticPage STATIC_PAGE = new StaticPage("intro-page-title", "intro-page-content");
    private static final StaticPageContent STATIC_PAGE_CONTENT = StaticPageContent.builder().page(STATIC_PAGE).seo(SEO_ATTRIBUTES).build();
    private static final NavigationItem NAVIGATION_ITEM = NavigationItem.build("link", "title");

    private static final String VIEW_GROUP_STATIC = "static";
    private static final String VIEW_PAGE = "page";

    @Mock
    private StaticPageContentFacade staticPageContentFacade;

    @Mock
    private NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @Mock
    private NavigationItemFactory<String> stringNavigationItemFactory;

    @InjectMocks
    private StaticPageController staticPageController;

    @Test
    public void shouldRenderIntroductionPage() {

        // given
        given(staticPageContentFacade.getStaticPage(StaticPageType.INTRODUCTION)).willReturn(STATIC_PAGE_CONTENT);
        given(navigationItemFactoryRegistry.getFactory(String.class)).willReturn(stringNavigationItemFactory);
        given(stringNavigationItemFactory.create(STATIC_PAGE.getTitle())).willReturn(NAVIGATION_ITEM);

        // when
        staticPageController.introduction();

        // then
        verifyViewCreated(VIEW_PAGE);
        verifyFieldSet(ModelField.STATIC, STATIC_PAGE);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM);
        verifySeoOverride(SEO_ATTRIBUTES);
    }

    @Override
    String controllerViewGroup() {
        return VIEW_GROUP_STATIC;
    }
}
