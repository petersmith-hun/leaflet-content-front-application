package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.StaticPageType;
import hu.psprog.leaflet.lcfa.core.facade.StaticPageContentFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
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
public class StaticPageController {

    private static final String VIEW_STATIC_PAGE = "view/static/page";

    private ModelAndViewFactory modelAndViewFactory;
    private StaticPageContentFacade staticPageContentFacade;

    @Autowired
    public StaticPageController(ModelAndViewFactory modelAndViewFactory, StaticPageContentFacade staticPageContentFacade) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.staticPageContentFacade = staticPageContentFacade;
    }

    /**
     * GET /introduction
     * Renders introduction page.
     *
     * @return populated {@link ModelAndView} object
     */
    @GetMapping("/introduction")
    public ModelAndView introduction() {
        return renderPage(StaticPageType.INTRODUCTION);
    }

    private ModelAndView renderPage(StaticPageType staticPageType) {

        StaticPageContent staticPageContent = staticPageContentFacade.getStaticPage(staticPageType);

        return modelAndViewFactory.createForView(VIEW_STATIC_PAGE)
                .withAttribute(ModelField.STATIC, staticPageContent.getPage())
                .withAttribute(CommonPageDataField.SEO_ATTRIBUTES.getFieldName(), staticPageContent.getSeo())
                .build();
    }
}
