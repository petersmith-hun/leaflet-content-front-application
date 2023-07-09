package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.Article;
import hu.psprog.leaflet.lcfa.core.domain.content.ArticleContent;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import hu.psprog.leaflet.lcfa.core.facade.ArticleOperationFacade;
import hu.psprog.leaflet.lcfa.core.facade.BlogContentFacade;
import hu.psprog.leaflet.lcfa.web.auth.mock.WithMockedJWTUser;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl.NavigationItemFactoryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit tests for {@link ArticleController}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(MockitoExtension.class),
        @ExtendWith(SpringExtension.class)
})
@WithMockedJWTUser
public class ArticleControllerTest extends AbstractControllerTest {

    private static final String LINK = "link";
    private static final String REDIRECTION_PATH = "/article/" + LINK;
    private static final CategorySummary CATEGORY_SUMMARY = CategorySummary.builder().title("category").build();
    private static final TagSummary TAG_SUMMARY = TagSummary.builder().name("tag").build();
    private static final SEOAttributes SEO_ATTRIBUTES = SEOAttributes.builder().pageTitle("seo-page-title").build();
    private static final CommentSummary COMMENT_SUMMARY = CommentSummary.builder().content("comment").build();
    private static final Article ARTICLE = Article.builder().category(CATEGORY_SUMMARY).content("article-content").build();
    private static final List<CategorySummary> CATEGORY_SUMMARY_LIST = Collections.singletonList(CATEGORY_SUMMARY);
    private static final List<TagSummary> TAG_SUMMARY_LIST = Collections.singletonList(TAG_SUMMARY);
    private static final List<CommentSummary> COMMENT_SUMMARY_LIST = Collections.singletonList(COMMENT_SUMMARY);
    private static final ArticleContent ARTICLE_CONTENT = ArticleContent.builder()
            .categories(CATEGORY_SUMMARY_LIST)
            .tags(TAG_SUMMARY_LIST)
            .seo(SEO_ATTRIBUTES)
            .comments(COMMENT_SUMMARY_LIST)
            .article(ARTICLE)
            .build();
    private static final NavigationItem NAVIGATION_ITEM_ARTICLE = NavigationItem.build("link-article", "title-article");
    private static final NavigationItem NAVIGATION_ITEM_CATEGORY = NavigationItem.build("link-category", "title-category");

    private static final ArticleCommentRequest ARTICLE_COMMENT_REQUEST = new ArticleCommentRequest();

    private static final String VIEW_GROUP_BLOG = "blog";
    private static final String VIEW_ARTICLE = "article";

    static {
        ARTICLE_COMMENT_REQUEST.setMessage("message");
    }

    @Mock
    private BlogContentFacade blogContentFacade;

    @Mock
    private ArticleOperationFacade articleOperationFacade;

    @Mock
    private NavigationItemFactoryRegistry navigationItemFactoryRegistry;

    @Mock
    private NavigationItemFactory<Article> articleNavigationItemFactory;

    @Mock
    private NavigationItemFactory<CategorySummary> categorySummaryNavigationItemFactory;

    private ArticleController articleController;

    @BeforeEach
    public void setup() {
        super.setup();
        articleController = new ArticleController(modelAndViewFactory, blogContentFacade, articleOperationFacade, navigationItemFactoryRegistry);
    }

    @Test
    public void shouldShowArticle() {

        // given
        givenArticlePage();

        // when
        articleController.showArticle(LINK, ARTICLE_COMMENT_REQUEST);

        // then
        verifyArticlePage();
    }

    @Test
    public void shouldProcessCommentRequestWithSuccess() {

        // given
        given(articleOperationFacade.processCommentRequest(ARTICLE_COMMENT_REQUEST)).willReturn(true);

        // when
        articleController.processCommentRequest(LINK, ARTICLE_COMMENT_REQUEST, bindingResult, redirectAttributes);

        // then
        verifyFlashMessageSet(FlashMessageKey.SUCCESSFUL_COMMENT_REQUEST);
        verifyRedirectionCreated(REDIRECTION_PATH);
    }

    @Test
    public void shouldProcessCommentRequestWithFailure() {

        // given
        given(articleOperationFacade.processCommentRequest(ARTICLE_COMMENT_REQUEST)).willReturn(false);

        // when
        articleController.processCommentRequest(LINK, ARTICLE_COMMENT_REQUEST, bindingResult, redirectAttributes);

        // then
        verifyFlashMessageSet(FlashMessageKey.FAILED_COMMENT_REQUEST);
        verifyRedirectionCreated(REDIRECTION_PATH);
    }

    @Test
    public void shouldProcessCommentRequestWithValidationFailure() {

        // given
        givenArticlePage();
        given(bindingResult.hasErrors()).willReturn(true);

        // when
        articleController.processCommentRequest(LINK, ARTICLE_COMMENT_REQUEST, bindingResult, redirectAttributes);

        // then
        verifyArticlePage();
        verifyNoMoreInteractions(articleOperationFacade);
    }

    @Override
    String controllerViewGroup() {
        return VIEW_GROUP_BLOG;
    }

    private void givenArticlePage() {
        given(blogContentFacade.getArticle(LINK)).willReturn(ARTICLE_CONTENT);
        given(navigationItemFactoryRegistry.getFactory(Article.class)).willReturn(articleNavigationItemFactory);
        given(navigationItemFactoryRegistry.getFactory(CategorySummary.class)).willReturn(categorySummaryNavigationItemFactory);
        given(articleNavigationItemFactory.create(ARTICLE)).willReturn(NAVIGATION_ITEM_ARTICLE);
        given(categorySummaryNavigationItemFactory.create(CATEGORY_SUMMARY)).willReturn(NAVIGATION_ITEM_CATEGORY);
    }

    private void verifyArticlePage() {
        verifyViewCreated(VIEW_ARTICLE);
        verifyFieldSet(ModelField.ARTICLE, ARTICLE);
        verifyFieldSet(ModelField.COMMENTS, COMMENT_SUMMARY_LIST);
        verifyFieldSet(ModelField.LIST_CATEGORIES, CATEGORY_SUMMARY_LIST);
        verifyFieldSet(ModelField.LIST_TAGS, TAG_SUMMARY_LIST);
        verifyFieldSet(ModelField.VALIDATED_MODEL, ARTICLE_COMMENT_REQUEST);
        verifyFieldSet(ModelField.NAVIGATION, Arrays.asList(NAVIGATION_ITEM_CATEGORY, NAVIGATION_ITEM_ARTICLE));
        verifySeoOverride(SEO_ATTRIBUTES);
    }
}
