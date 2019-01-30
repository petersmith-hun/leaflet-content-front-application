package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * Controller implementation for home page.
 * The controller will list of the most recent blog entries.
 *
 * @author Peter Smith
 */
@Controller
@RequestMapping({"/", "/{page}"})
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
     * GET /[{page}]
     * Renders home page.
     *
     * @param optionalPageNumber number of page to be requested (optional, defaults to DEFAULT_PAGE_NUMBER)
     * @return populated {@link ModelAndView}
     */
    @GetMapping
    public ModelAndView getHomePage(@PathVariable(required = false, value = "page") Optional<Integer> optionalPageNumber) {

        int pageNumber = optionalPageNumber.orElse(DEFAULT_PAGE_NUMBER);
        HomePageContent homePageContent = blogContentFacade.getHomePageContent(pageNumber);

        return modelAndViewFactory.createForView(VIEW_BLOG_HOME)
                .withAttribute(ModelField.LIST_ENTRIES, homePageContent.getEntries())
                .withAttribute(ModelField.LIST_CATEGORIES, homePageContent.getCategories())
                .withAttribute(ModelField.LIST_TAGS, homePageContent.getTags())
                .withAttribute(ModelField.PAGINATION, homePageContent.getPagination())
                .withAttribute(ModelField.CURRENT_PAGE_NUMBER, pageNumber)
                .build();
    }
}
