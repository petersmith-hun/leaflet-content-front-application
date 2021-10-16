package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.domain.request.AccountRequestWrapper;
import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link ArticleOperationFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
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

    @ParameterizedTest
    @MethodSource("successfulCommentRequestDataProvider")
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

    @ParameterizedTest
    @MethodSource("invalidCommentRequestDataProvider")
    public void shouldProcessCommentRequestWithFailureForInvalidRequests(ArticleCommentRequest articleCommentRequest) {

        // when
        boolean result = articleOperationFacade.processCommentRequest(null, articleCommentRequest);

        // then
        assertThat(result, is(false));
        verifyNoInteractions(contentRequestAdapterRegistry, commentPostContentRequestAdapter);
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

    private static Stream<Arguments> successfulCommentRequestDataProvider() {

        return Stream.of(
                Arguments.of(null, ANONYMOUS_ARTICLE_COMMENT_REQUEST),
                Arguments.of(USER_ID, AUTHENTICATED_ARTICLE_COMMENT_REQUEST)
        );
    }

    private static Stream<Arguments> invalidCommentRequestDataProvider() {

        return Stream.of(
                Arguments.of(AUTHENTICATED_ARTICLE_COMMENT_REQUEST), // without passing the user ID
                Arguments.of(ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_EMAIL),
                Arguments.of(ANONYMOUS_ARTICLE_COMMENT_REQUEST_WITH_MISSING_NAME)
        );
    }
}
