package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller implementation for rendering articles.
 *
 * @author Peter Smith
 */
@Controller
@RequestMapping("/article/{link}")
public class ArticleController {

    private static final String VIEW_BLOG_DETAILS = "view/blog/article";

    private ModelAndViewFactory modelAndViewFactory;
    private BlogContentFacade blogContentFacade;

    @Autowired
    public ArticleController(ModelAndViewFactory modelAndViewFactory, BlogContentFacade blogContentFacade) {
        this.modelAndViewFactory = modelAndViewFactory;
        this.blogContentFacade = blogContentFacade;
    }

    @GetMapping
    public ModelAndView showArticle(@PathVariable("link") String link) {

        return modelAndViewFactory.createForView(VIEW_BLOG_DETAILS)
                .withAttribute(ModelField.ARTICLE, blogContentFacade.getArticle(link))
                .build();
    }
}
