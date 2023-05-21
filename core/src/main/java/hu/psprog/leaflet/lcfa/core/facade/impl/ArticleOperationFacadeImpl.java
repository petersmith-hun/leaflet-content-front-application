package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.domain.request.AccountRequestWrapper;
import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import hu.psprog.leaflet.lcfa.core.facade.ArticleOperationFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Implementation of {@link ArticleOperationFacade}.
 *
 * @author Peter Smith
 */
@Service
public class ArticleOperationFacadeImpl implements ArticleOperationFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleOperationFacadeImpl.class);

    private final ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Autowired
    public ArticleOperationFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
    }

    @Override
    public boolean processCommentRequest(Long userID, ArticleCommentRequest articleCommentRequest) {

        boolean valid = isRequestValid(userID, articleCommentRequest);
        boolean successful = false;
        if (valid) {
            successful = contentRequestAdapterRegistry.<Boolean, AccountRequestWrapper<ArticleCommentRequest>>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_POST)
                    .getContent(new AccountRequestWrapper<>(userID, articleCommentRequest))
                    .orElse(false);
        } else {
            LOGGER.warn("Comment creation request received in inconsistent state for userID [{}] with request data [{}]", userID, articleCommentRequest);
        }

        return successful;
    }

    private boolean isRequestValid(Long userID, ArticleCommentRequest articleCommentRequest) {
        return isUserIdPresent(userID) || isAnonymousUserInfoPresent(articleCommentRequest);
    }

    private boolean isUserIdPresent(Long userID) {
        return Objects.nonNull(userID);
    }

    private boolean isAnonymousUserInfoPresent(ArticleCommentRequest articleCommentRequest) {
        return Objects.nonNull(articleCommentRequest.getEmail()) && Objects.nonNull(articleCommentRequest.getName());
    }
}
