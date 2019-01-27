package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller implementation for home page.
 * The controller will list of the most recent blog entries.
 *
 * @author Peter Smith
 */
@Controller
@RequestMapping("/")
public class HomeController {

    private static final String VIEW_BLOG_HOME = "view/blog/home";
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private ModelAndViewFactory modelAndViewFactory;
    private BlogContentFacade blogContentFacade;

    @Autowired
    public HomeController(ModelAndViewFactory modelAndViewFactory, BlogContentFacade blogContentFacade) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.blogContentFacade = blogContentFacade;
    }

    /**
     * GET /
     * Renders home page.
     *
     * @return populated {@link ModelAndView}
     */
    @GetMapping
    public ModelAndView getHomePage() {

        HomePageContent homePageContent = blogContentFacade.getHomePageContent(DEFAULT_PAGE_NUMBER);

        return modelAndViewFactory.createForView(VIEW_BLOG_HOME)
                .withAttribute(ModelField.LIST_ENTRIES.getFieldName(), homePageContent.getEntries())
                .withAttribute(ModelField.LIST_CATEGORIES.getFieldName(), homePageContent.getCategories())
                .withAttribute(ModelField.LIST_TAGS.getFieldName(), homePageContent.getTags())
                .withAttribute(ModelField.PAGINATION.getFieldName(), homePageContent.getPagination())
                .build();
    }
}
