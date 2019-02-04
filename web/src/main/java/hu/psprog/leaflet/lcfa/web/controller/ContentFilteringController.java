package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.content.HomePageContent;
import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * Controller implementation for content filtering related operations.
 * Currently content can be filtered in three ways:
 *  - by category
 *  - by tag
 *  - by content expression (free text)
 *
 * @author Peter Smith
 */
@Controller
public class ContentFilteringController {

    private static final String VIEW_BLOG_LIST = "view/blog/list";
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private static final String PAGINATION_LINK_PREFIX_CATEGORY = "/category/%d/%s";
    private static final String PAGINATION_LINK_PREFIX_TAG = "/tag/%d/%s";
    private static final String PAGINATION_LINK_PREFIX_CONTENT = "/content";
    private static final String PAGINATION_LINK_SUFFIX_CONTENT = "?content=%s";

    private BlogContentFacade blogContentFacade;
    private ModelAndViewFactory modelAndViewFactory;

    @Autowired
    public ContentFilteringController(BlogContentFacade blogContentFacade, ModelAndViewFactory modelAndViewFactory) {
        this.blogContentFacade = blogContentFacade;
        this.modelAndViewFactory = modelAndViewFactory;
    }

    /**
     * GET /category/{categoryID}/{categoryAlias}[/page/{page}]
     * Renders filtered article list by category.
     *
     * @param optionalPageNumber number of page to be requested (optional, defaults to DEFAULT_PAGE_NUMBER)
     * @return populated {@link ModelAndView}
     */
    @GetMapping({"/category/{categoryID}/{categoryAlias}", "/category/{categoryID}/{categoryAlias}/page/{page}"})
    public ModelAndView getArticleListByCategory(@PathVariable("categoryID") long categoryID,
                                                 @PathVariable("categoryAlias") String categoryAlias,
                                                 @PathVariable(required = false, value = "page") Optional<Integer> optionalPageNumber) {

        int pageNumber = extractPageNumber(optionalPageNumber);
        HomePageContent homePageContent = blogContentFacade.getArticlesByCategory(categoryID, pageNumber);
        String paginationLinkPrefix = String.format(PAGINATION_LINK_PREFIX_CATEGORY, categoryID, categoryAlias);

        return populateModelAndView(paginationLinkPrefix, homePageContent, pageNumber);
    }

    /**
     * GET /tag/{tagID}/{tagAlias}[/page/{page}]
     * Renders filtered article list by tag.
     *
     * @param optionalPageNumber number of page to be requested (optional, defaults to DEFAULT_PAGE_NUMBER)
     * @return populated {@link ModelAndView}
     */
    @GetMapping({"/tag/{tagID}/{tagAlias}", "/tag/{tagID}/{tagAlias}/page/{page}"})
    public ModelAndView getArticleListByTag(@PathVariable("tagID") long tagID,
                                            @PathVariable("tagAlias") String tagAlias,
                                            @PathVariable(required = false, value = "page") Optional<Integer> optionalPageNumber) {

        int pageNumber = extractPageNumber(optionalPageNumber);
        HomePageContent homePageContent = blogContentFacade.getArticlesByTag(tagID, pageNumber);
        String paginationLinkPrefix = String.format(PAGINATION_LINK_PREFIX_TAG, tagID, tagAlias);

        return populateModelAndView(paginationLinkPrefix, homePageContent, pageNumber);
    }

    /**
     * GET /content[/page/{page}]?content
     * Renders filtered article list by content.
     *
     * @param optionalPageNumber number of page to be requested (optional, defaults to DEFAULT_PAGE_NUMBER)
     * @return populated {@link ModelAndView}
     */
    @GetMapping({"/content", "/content/page/{page}"})
    public ModelAndView getArticleListByContent(@RequestParam(value = "content") String contentExpression,
                                                @PathVariable(required = false, value = "page") Optional<Integer> optionalPageNumber) {

        int pageNumber = extractPageNumber(optionalPageNumber);
        HomePageContent homePageContent = blogContentFacade.getArticlesByContent(contentExpression, pageNumber);
        String paginationLinkSuffix = String.format(PAGINATION_LINK_SUFFIX_CONTENT, contentExpression);

        return populateModelAndView(PAGINATION_LINK_PREFIX_CONTENT, paginationLinkSuffix,  homePageContent, pageNumber);
    }

    private int extractPageNumber(Optional<Integer> optionalPageNumber) {
        return optionalPageNumber.orElse(DEFAULT_PAGE_NUMBER);
    }

    private ModelAndView populateModelAndView(String paginationLinkPrefix, HomePageContent homePageContent, int pageNumber) {
        return populateModelAndView(paginationLinkPrefix, StringUtils.EMPTY, homePageContent, pageNumber);
    }

    private ModelAndView populateModelAndView(String paginationLinkPrefix, String paginationLinkSuffix, HomePageContent homePageContent, int pageNumber) {
        return modelAndViewFactory.createForView(VIEW_BLOG_LIST)
                .withAttribute(ModelField.LIST_ENTRIES, homePageContent.getEntries())
                .withAttribute(ModelField.LIST_CATEGORIES, homePageContent.getCategories())
                .withAttribute(ModelField.LIST_TAGS, homePageContent.getTags())
                .withAttribute(ModelField.PAGINATION, homePageContent.getPagination())
                .withAttribute(ModelField.CURRENT_PAGE_NUMBER, pageNumber)
                .withAttribute(ModelField.PAGINATION_LINK_PREFIX, paginationLinkPrefix)
                .withAttribute(ModelField.PAGINATION_LINK_SUFFIX, paginationLinkSuffix)
                .build();
    }
}
