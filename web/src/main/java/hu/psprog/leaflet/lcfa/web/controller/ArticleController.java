package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;
import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import hu.psprog.leaflet.lcfa.core.facade.ArticleOperationFacade;
import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl.NavigationItemFactoryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Controller implementation for rendering articles and handling article related user operations.
 *
 * @author Peter Smith
 */
@Controller
public class ArticleController extends BaseController {

    private static final String VIEW_BLOG_DETAILS = "view/blog/article";

    private final ModelAndViewFactory modelAndViewFactory;
    private final BlogContentFacade blogContentFacade;
    private final ArticleOperationFacade articleOperationFacade;
    private final NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @Autowired
    public ArticleController(ModelAndViewFactory modelAndViewFactory, BlogContentFacade blogContentFacade,
                             ArticleOperationFacade articleOperationFacade, NavigationItemFactoryRegistry navigationItemFactoryRegistry) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.blogContentFacade = blogContentFacade;
        this.articleOperationFacade = articleOperationFacade;
        this.navigationItemFactoryRegistry = navigationItemFactoryRegistry;
    }

    /**
     * GET /article/{link}
     * Renders an article.
     *
     * @param link link of the article
     * @return populated {@link ModelAndView} object
     */
    @GetMapping(PATH_ARTICLE_BY_LINK)
    public ModelAndView showArticle(@PathVariable("link") String link, @ModelAttribute ArticleCommentRequest articleCommentRequest) {

        ArticleContent articleContent = blogContentFacade.getArticle(link);

        return modelAndViewFactory.createForView(VIEW_BLOG_DETAILS)
                .withAttribute(ModelField.ARTICLE, articleContent.article())
                .withAttribute(ModelField.COMMENTS, articleContent.comments())
                .withAttribute(ModelField.LIST_CATEGORIES, articleContent.categories())
                .withAttribute(ModelField.LIST_TAGS, articleContent.tags())
                .withAttribute(CommonPageDataField.SEO_ATTRIBUTES.getFieldName(), articleContent.seo())
                .withAttribute(ModelField.VALIDATED_MODEL, articleCommentRequest)
                .withAttribute(ModelField.NAVIGATION, createNavigation(articleContent))
                .build();
    }

    /**
     * POST /article/{link}/comment
     * Processes an article comment request.
     *
     * @param link link of the article
     * @param articleCommentRequest form contents as {@link ArticleCommentRequest}
     * @param bindingResult validation results
     * @param redirectAttributes redirection attributes
     * @return populated {@link ModelAndView} object
     */
    @PostMapping(PATH_COMMENT)
    public ModelAndView processCommentRequest(@PathVariable("link") String link, @ModelAttribute @Valid ArticleCommentRequest articleCommentRequest,
                                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView;
        if (bindingResult.hasErrors()) {
            modelAndView = showArticle(link, articleCommentRequest);
        } else {
            FlashMessageKey flashMessageKey = articleOperationFacade.processCommentRequest(currentUserID(), articleCommentRequest)
                    ? FlashMessageKey.SUCCESSFUL_COMMENT_REQUEST
                    : FlashMessageKey.FAILED_COMMENT_REQUEST;
            flash(redirectAttributes, flashMessageKey);
            modelAndView = modelAndViewFactory.createRedirectionTo(replaceEntryLinkInPath(link));
        }

        return modelAndView;
    }

    private List<NavigationItem> createNavigation(ArticleContent articleContent) {
        return Arrays.asList(getCategoryNavigationItem(articleContent), getArticleNavigationItem(articleContent));
    }

    private NavigationItem getArticleNavigationItem(ArticleContent articleContent) {
        return navigationItemFactoryRegistry
                .getFactory(Article.class)
                .create(articleContent.article());
    }

    private NavigationItem getCategoryNavigationItem(ArticleContent articleContent) {
        return navigationItemFactoryRegistry
                .getFactory(CategorySummary.class)
                .create(articleContent.article().category());
    }

    private String replaceEntryLinkInPath(String link) {
        return PATH_ARTICLE_BY_LINK.replace("{link}", link);
    }
}
