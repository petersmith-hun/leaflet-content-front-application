package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import hu.psprog.leaflet.lcfa.core.facade.ArticleOperationFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ArticleOperationFacade}.
 *
 * @author Peter Smith
 */
@Service
public class ArticleOperationFacadeImpl implements ArticleOperationFacade {

    private final ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Autowired
    public ArticleOperationFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
    }

    @Override
    public boolean processCommentRequest(ArticleCommentRequest articleCommentRequest) {

        return contentRequestAdapterRegistry.<Boolean, ArticleCommentRequest>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_POST)
                .getContent(articleCommentRequest)
                .orElse(false);
    }
}
