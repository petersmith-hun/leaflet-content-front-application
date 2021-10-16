package hu.psprog.leaflet.lcfa.core.facade.impl.utility;

import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CommentCreateRequestFactory}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommentCreateRequestFactoryTest {

    private static final long AUTHENTICATED_USER_ID = 8L;
    private static final long ENTRY_ID = 12L;
    private static final String MESSAGE = "message";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final ArticleCommentRequest ARTICLE_COMMENT_REQUEST = new ArticleCommentRequest();
    private static final CommentCreateRequestModel AUTHENTICATED_REQUEST_MODEL = new CommentCreateRequestModel();
    private static final CommentCreateRequestModel ANONYMOUS_REQUEST_MODEL = new CommentCreateRequestModel();

    static {
        ARTICLE_COMMENT_REQUEST.setEntryId(ENTRY_ID);
        ARTICLE_COMMENT_REQUEST.setMessage(MESSAGE);
        ARTICLE_COMMENT_REQUEST.setName(NAME);
        ARTICLE_COMMENT_REQUEST.setEmail(EMAIL);

        AUTHENTICATED_REQUEST_MODEL.setAuthenticatedUserId(AUTHENTICATED_USER_ID);
        AUTHENTICATED_REQUEST_MODEL.setEntryId(ENTRY_ID);
        AUTHENTICATED_REQUEST_MODEL.setContent(MESSAGE);

        ANONYMOUS_REQUEST_MODEL.setUsername(NAME);
        ANONYMOUS_REQUEST_MODEL.setEmail(EMAIL);
        ANONYMOUS_REQUEST_MODEL.setEntryId(ENTRY_ID);
        ANONYMOUS_REQUEST_MODEL.setContent(MESSAGE);
    }

    @InjectMocks
    private CommentCreateRequestFactory commentCreateRequestFactory;

    @Test
    public void shouldCreateCommentCreateRequestForAuthenticatedUser() {

        // when
        CommentCreateRequestModel result = commentCreateRequestFactory.create(AUTHENTICATED_USER_ID, ARTICLE_COMMENT_REQUEST);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(AUTHENTICATED_REQUEST_MODEL));
    }

    @Test
    public void shouldCreateCommentCreateRequestForAnonymousUser() {

        // when
        CommentCreateRequestModel result = commentCreateRequestFactory.create(null, ARTICLE_COMMENT_REQUEST);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ANONYMOUS_REQUEST_MODEL));
    }
}
