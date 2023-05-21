package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.bridge.service.TagBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.CallType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Abstract {@link AbstractParallelContentRequestAdapter} implementation adding the common Bridge call to the map of callers.
 *
 * @author Peter Smith
 */
abstract class AbstractFilteringSupportParallelContentRequestAdapter<T, P> extends AbstractParallelContentRequestAdapter<T, P> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFilteringSupportParallelContentRequestAdapter.class);

    private final CategoryBridgeService categoryBridgeService;
    private final TagBridgeService tagBridgeService;

    public AbstractFilteringSupportParallelContentRequestAdapter(AsyncTaskExecutor contentAdapterExecutor,
                                                                 CategoryBridgeService categoryBridgeService,
                                                                 TagBridgeService tagBridgeService) {
        super(contentAdapterExecutor);
        this.categoryBridgeService = categoryBridgeService;
        this.tagBridgeService = tagBridgeService;
    }

    @Override
    Map<CallType, Callable<BaseBodyDataModel>> callers(P contentRequestParameter) {

        Map<CallType, Callable<BaseBodyDataModel>> callableMap = new HashMap<>();
        callableMap.put(CallType.CATEGORY, wrapOptionalCall(categoryBridgeService::getPublicCategories));
        callableMap.put(CallType.TAG, wrapOptionalCall(tagBridgeService::getAllPublicTags));
        addContentCalls(callableMap, contentRequestParameter);

        return callableMap;
    }

    /**
     * Adds the actual content call(s) to the map of callers.
     *
     * @param callableMap current map of callers
     * @param contentRequestParameter additional request parameter object (can be null)
     */
    abstract void addContentCalls(Map<CallType, Callable<BaseBodyDataModel>> callableMap, P contentRequestParameter);

    private Callable<BaseBodyDataModel> wrapOptionalCall(Callable<BaseBodyDataModel> unwrappedCallable) {

        return () -> {

            BaseBodyDataModel response = null;
            try {
                response = unwrappedCallable.call();
            } catch (Exception e) {
                LOGGER.warn("Failed to retrieve optional filtering support data", e);
            }

            return response;
        };
    }
}
