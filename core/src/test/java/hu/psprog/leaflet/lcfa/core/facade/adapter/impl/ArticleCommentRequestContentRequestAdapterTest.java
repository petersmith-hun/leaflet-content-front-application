package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountRequestWrapper;
import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import hu.psprog.leaflet.lcfa.core.exception.UserSessionInvalidationRequiredException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.impl.utility.CommentCreateRequestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link ArticleCommentRequestContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ArticleCommentRequestContentRequestAdapterTest {

    private static final long USER_ID = 1L;
    private static final ArticleCommentRequest ARTICLE_COMMENT_REQUEST = new ArticleCommentRequest();
    private static final CommentCreateRequestModel COMMENT_CREATE_REQUEST_MODEL = new CommentCreateRequestModel();
    private static final String RECAPTCHA_TOKEN = "recaptcha";

    static {
        ARTICLE_COMMENT_REQUEST.setRecaptchaToken(RECAPTCHA_TOKEN);
    }

    @Mock
    private CommentBridgeService commentBridgeService;

    @Mock
    private CommentCreateRequestFactory commentCreateRequestFactory;

    @InjectMocks
    private ArticleCommentRequestContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(commentCreateRequestFactory.create(USER_ID, ARTICLE_COMMENT_REQUEST)).willReturn(COMMENT_CREATE_REQUEST_MODEL);

        // when
        Optional<Boolean> result = adapter.getContent(new AccountRequestWrapper<>(USER_ID, ARTICLE_COMMENT_REQUEST));

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(true));
        verify(commentBridgeService).createComment(COMMENT_CREATE_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetContentReturnThrowUserSessionInvalidationRequiredExceptionIfResponseIs401() throws CommunicationFailureException {

        // given
        given(commentCreateRequestFactory.create(USER_ID, ARTICLE_COMMENT_REQUEST)).willReturn(COMMENT_CREATE_REQUEST_MODEL);
        doThrow(UnauthorizedAccessException.class).when(commentBridgeService).createComment(COMMENT_CREATE_REQUEST_MODEL, RECAPTCHA_TOKEN);

        // when
        Assertions.assertThrows(UserSessionInvalidationRequiredException.class,
                () -> adapter.getContent(new AccountRequestWrapper<>(USER_ID, ARTICLE_COMMENT_REQUEST)));

        // then
        // exception expected
    }

    @Test
    public void shouldGetContentReturnWithFailureForAnyBridgeException() throws CommunicationFailureException {

        // given
        given(commentCreateRequestFactory.create(USER_ID, ARTICLE_COMMENT_REQUEST)).willReturn(COMMENT_CREATE_REQUEST_MODEL);
        doThrow(CommunicationFailureException.class).when(commentBridgeService).createComment(COMMENT_CREATE_REQUEST_MODEL, RECAPTCHA_TOKEN);

        // when
        Optional<Boolean> result = adapter.getContent(new AccountRequestWrapper<>(USER_ID, ARTICLE_COMMENT_REQUEST));

        // then
        assertThat(result.isPresent(), is(false));
        verify(commentBridgeService).createComment(COMMENT_CREATE_REQUEST_MODEL, RECAPTCHA_TOKEN);
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.COMMENT_POST));
    }
}
