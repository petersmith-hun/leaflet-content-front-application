package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.bridge.service.TagBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.CallType;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * {@link AbstractFilteringSupportParallelContentRequestAdapter} implementation returning all the necessary information for article page rendering.
 * Requests public lists of categories, public list of tags and the requested article (by its identifier link).
 * Combinator implementation returns the responses as {@link ArticlePageRawResponseWrapper} for further processing.
 *
 * @author Peter Smith
 */
@Component
public class ArticleContentRequestAdapter extends AbstractFilteringSupportParallelContentRequestAdapter<ArticlePageRawResponseWrapper, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleContentRequestAdapter.class);

    private static final int COMMENT_PAGE = 1;
    private static final int COMMENT_LIMIT = 1000;
    private static final OrderBy.Comment COMMENT_ORDER_BY = OrderBy.Comment.CREATED;
    private static final OrderDirection COMMENT_ORDER_DIRECTION = OrderDirection.DESC;

    private final EntryBridgeService entryBridgeService;
    private final CommentBridgeService commentBridgeService;

    @Autowired
    public ArticleContentRequestAdapter(@Qualifier("contentAdapterAsyncTaskExecutor") AsyncTaskExecutor contentAdapterExecutor,
                                        CategoryBridgeService categoryBridgeService,
                                        TagBridgeService tagBridgeService,
                                        EntryBridgeService entryBridgeService,
                                        CommentBridgeService commentBridgeService) {
        super(contentAdapterExecutor, categoryBridgeService, tagBridgeService);
        this.entryBridgeService = entryBridgeService;
        this.commentBridgeService = commentBridgeService;
    }

    @Override
    void addContentCalls(Map<CallType, Callable<BaseBodyDataModel>> callableMap, String contentRequestParameter) {
        callableMap.put(CallType.ENTRY, () -> entryBridgeService.getEntryByLink(contentRequestParameter));
        callableMap.put(CallType.COMMENT, prepareCommentBridgeCallable(contentRequestParameter));
    }

    @Override
    ArticlePageRawResponseWrapper combinator(Map<CallType, BaseBodyDataModel> result) {

        ArticlePageRawResponseWrapper articlePageRawResponseWrapper = null;
        WrapperBodyDataModel<ExtendedEntryDataModel> entry = (WrapperBodyDataModel<ExtendedEntryDataModel>) result.get(CallType.ENTRY);
        if (Objects.nonNull(entry)) {
            articlePageRawResponseWrapper = ArticlePageRawResponseWrapper.builder()
                    .categoryListDataModel((CategoryListDataModel) result.get(CallType.CATEGORY))
                    .wrappedExtendedEntryDataModel(entry)
                    .wrappedTagListDataModel((WrapperBodyDataModel<TagListDataModel>) result.get(CallType.TAG))
                    .wrappedCommentListDataModel((WrapperBodyDataModel<CommentListDataModel>) result.get(CallType.COMMENT))
                    .build();
        }

        return articlePageRawResponseWrapper;
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.ARTICLE;
    }

    private Callable<BaseBodyDataModel> prepareCommentBridgeCallable(String contentRequestParameter) {
        return () -> {

            WrapperBodyDataModel<CommentListDataModel> pageOfPublicCommentsForEntry = null;
            try {
                // pagination does not seem to be needed now, so returning the first 1000 comments will do for now
                pageOfPublicCommentsForEntry = commentBridgeService
                        .getPageOfPublicCommentsForEntry(contentRequestParameter, COMMENT_PAGE, COMMENT_LIMIT, COMMENT_ORDER_BY, COMMENT_ORDER_DIRECTION);
            } catch (Exception e) {
                LOGGER.warn("Failed to retrieve comments for entry identified by [{}]", contentRequestParameter, e);
            }

            return pageOfPublicCommentsForEntry;
        };
    }
}
