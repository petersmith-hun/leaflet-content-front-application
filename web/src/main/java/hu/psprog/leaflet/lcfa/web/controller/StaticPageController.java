package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;
import hu.psprog.leaflet.lcfa.core.facade.StaticPageContentFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl.NavigationItemFactoryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller implementation for rendering static pages.
 *
 * @author Peter Smith
 */
@Controller
public class StaticPageController extends BaseController {

    private static final String VIEW_STATIC_PAGE = "view/static/page";

    private ModelAndViewFactory modelAndViewFactory;
    private StaticPageContentFacade staticPageContentFacade;
    private NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @Autowired
    public StaticPageController(ModelAndViewFactory modelAndViewFactory, StaticPageContentFacade staticPageContentFacade,
                                NavigationItemFactoryRegistry navigationItemFactoryRegistry) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.staticPageContentFacade = staticPageContentFacade;
        this.navigationItemFactoryRegistry = navigationItemFactoryRegistry;
    }

    /**
     * GET /introduction
     * Renders introduction page.
     *
     * @return populated {@link ModelAndView} object
     */
    @GetMapping(PATH_INTRODUCTION)
    public ModelAndView introduction() {
        return renderPage(StaticPageType.INTRODUCTION);
    }

    private ModelAndView renderPage(StaticPageType staticPageType) {

        StaticPageContent staticPageContent = staticPageContentFacade.getStaticPage(staticPageType);

        return modelAndViewFactory.createForView(VIEW_STATIC_PAGE)
                .withAttribute(ModelField.STATIC, staticPageContent.getPage())
                .withAttribute(CommonPageDataField.SEO_ATTRIBUTES.getFieldName(), staticPageContent.getSeo())
                .withAttribute(ModelField.NAVIGATION, navigationItemFactoryRegistry
                        .getFactory(String.class)
                        .create(staticPageContent.getPage().getTitle()))
                .build();
    }
}
