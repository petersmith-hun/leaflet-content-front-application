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

    private static final long ENTRY_ID = 12L;
    private static final String MESSAGE = "message";
    private static final ArticleCommentRequest ARTICLE_COMMENT_REQUEST = new ArticleCommentRequest();
    private static final CommentCreateRequestModel COMMENT_CREATE_REQUEST_MODEL = new CommentCreateRequestModel();

    static {
        ARTICLE_COMMENT_REQUEST.setEntryId(ENTRY_ID);
        ARTICLE_COMMENT_REQUEST.setMessage(MESSAGE);

        COMMENT_CREATE_REQUEST_MODEL.setEntryId(ENTRY_ID);
        COMMENT_CREATE_REQUEST_MODEL.setContent(MESSAGE);
    }

    @InjectMocks
    private CommentCreateRequestFactory commentCreateRequestFactory;

    @Test
    public void shouldCreateCommentCreateRequestForAuthenticatedUser() {

        // when
        CommentCreateRequestModel result = commentCreateRequestFactory.create(ARTICLE_COMMENT_REQUEST);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(COMMENT_CREATE_REQUEST_MODEL));
    }
}
