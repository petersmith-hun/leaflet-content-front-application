package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ArticleOperationFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ArticleOperationFacadeImplTest {

    private static final String MESSAGE = "message";
    private static final ArticleCommentRequest ARTICLE_COMMENT_REQUEST = new ArticleCommentRequest();

    static {
        ARTICLE_COMMENT_REQUEST.setMessage(MESSAGE);
    }

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private ContentRequestAdapter<Boolean, ArticleCommentRequest> commentPostContentRequestAdapter;

    @InjectMocks
    private ArticleOperationFacadeImpl articleOperationFacade;

    @Test
    public void shouldProcessCommentRequestWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, ArticleCommentRequest>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_POST))
                .willReturn(commentPostContentRequestAdapter);
        given(commentPostContentRequestAdapter.getContent(ARTICLE_COMMENT_REQUEST)).willReturn(Optional.of(true));

        // when
        boolean result = articleOperationFacade.processCommentRequest(ARTICLE_COMMENT_REQUEST);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldProcessCommentRequestWithFailureForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, ArticleCommentRequest>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_POST))
                .willReturn(commentPostContentRequestAdapter);
        given(commentPostContentRequestAdapter.getContent(ARTICLE_COMMENT_REQUEST)).willReturn(Optional.empty());

        // when
        boolean result = articleOperationFacade.processCommentRequest(ARTICLE_COMMENT_REQUEST);

        // then
        assertThat(result, is(false));
    }
}
