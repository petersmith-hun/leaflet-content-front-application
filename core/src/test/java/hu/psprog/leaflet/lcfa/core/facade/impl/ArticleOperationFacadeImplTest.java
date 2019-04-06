package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.domain.request.AccountRequestWrapper;
import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for {@link ArticleOperationFacadeImpl}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class ArticleOperationFacadeImplTest {

    private static final long USER_ID = 1L;
    private static final String MESSAGE = "message";
    private static final String EMAIL = "email";
    private static final String NAME = "name";

    private static final ArticleCommentRequest ANONYMOUS_ARTICLE_COMMENT_REQUEST = new ArticleCommentRequest();
    private static final ArticleCommentRequest ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_EMAIL = new ArticleCommentRequest();
    private static final ArticleCommentRequest ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_NAME = new ArticleCommentRequest();
    private static final ArticleCommentRequest AUTHENTICATED_ARTICLE_COMMENT_REQUEST = new ArticleCommentRequest();

    static {
        ANONYMOUS_ARTICLE_COMMENT_REQUEST.setMessage(MESSAGE);
        ANONYMOUS_ARTICLE_COMMENT_REQUEST.setEmail(EMAIL);
        ANONYMOUS_ARTICLE_COMMENT_REQUEST.setName(NAME);

        AUTHENTICATED_ARTICLE_COMMENT_REQUEST.setMessage(MESSAGE);

        ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_EMAIL.setName(NAME);
        ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_EMAIL.setMessage(MESSAGE);
        ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_NAME.setEmail(EMAIL);
        ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_NAME.setMessage(MESSAGE);
    }

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private ContentRequestAdapter<Boolean, AccountRequestWrapper<ArticleCommentRequest>> commentPostContentRequestAdapter;

    @InjectMocks
    private ArticleOperationFacadeImpl articleOperationFacade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(source = CommentRequestDataProvider.class, method = "successfulCases")
    public void shouldProcessCommentRequestWithSuccess(Long userID, ArticleCommentRequest articleCommentRequest) {

        // given
        given(contentRequestAdapterRegistry.<Boolean, AccountRequestWrapper<ArticleCommentRequest>>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_POST))
                .willReturn(commentPostContentRequestAdapter);
        given(commentPostContentRequestAdapter.getContent(new AccountRequestWrapper<>(userID, articleCommentRequest))).willReturn(Optional.of(true));

        // when
        boolean result = articleOperationFacade.processCommentRequest(userID, articleCommentRequest);

        // then
        assertThat(result, is(true));
    }

    @Test
    @Parameters(source = CommentRequestDataProvider.class, method = "invalidCases")
    public void shouldProcessCommentRequestWithFailureForInvalidRequests(ArticleCommentRequest articleCommentRequest) {

        // when
        boolean result = articleOperationFacade.processCommentRequest(null, articleCommentRequest);

        // then
        assertThat(result, is(false));
        verifyZeroInteractions(contentRequestAdapterRegistry, commentPostContentRequestAdapter);
    }

    @Test
    public void shouldProcessCommentRequestWithFailureForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, AccountRequestWrapper<ArticleCommentRequest>>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_POST))
                .willReturn(commentPostContentRequestAdapter);
        given(commentPostContentRequestAdapter.getContent(new AccountRequestWrapper<>(USER_ID, AUTHENTICATED_ARTICLE_COMMENT_REQUEST))).willReturn(Optional.empty());

        // when
        boolean result = articleOperationFacade.processCommentRequest(USER_ID, AUTHENTICATED_ARTICLE_COMMENT_REQUEST);

        // then
        assertThat(result, is(false));
    }

    public static class CommentRequestDataProvider {

        public static Object[] successfulCases() {

            return new Object[] {
                    new Object[] {null, ANONYMOUS_ARTICLE_COMMENT_REQUEST},
                    new Object[] {USER_ID, AUTHENTICATED_ARTICLE_COMMENT_REQUEST}
            };
        }

        public static Object[] invalidCases() {

            return new Object[] {
                    new Object[] {AUTHENTICATED_ARTICLE_COMMENT_REQUEST}, // without passing the user ID
                    new Object[] {ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_EMAIL},
                    new Object[] {ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_NAME}
            };
        }
    }
}
