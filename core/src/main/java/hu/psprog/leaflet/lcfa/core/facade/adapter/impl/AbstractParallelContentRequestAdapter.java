package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.lcfa.core.domain.CallType;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Abstract {@link ContentRequestAdapter} implementation handling async parallel backend requests.
 *
 * @param <T> return type
 * @param <P> input parameter type
 * @author Peter Smith
 */
abstract class AbstractParallelContentRequestAdapter<T, P> implements ContentRequestAdapter<T, P> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParallelContentRequestAdapter.class);

    @Autowired
    @Qualifier("contentAdapterAsyncTaskExecutor")
    private AsyncTaskExecutor contentAdapterExecutor;

    @Override
    public Optional<T> getContent(P contentRequestParameter) {

        T content = null;
        try {
            content = combinator(doCalls(contentRequestParameter));
        } catch (Exception e) {
            LOGGER.error("Threaded processing interrupted", e);
        }

        return Optional.ofNullable(content);
    }

    /**
     * Template method for declaring actual backend calls.
     * Actual calls should be declared as a map of a {@link CallType} (for differentiating the calls)
     * and a {@link Callable} implementation with a return type of {@link BaseBodyDataModel}.
     *
     * @param contentRequestParameter additional request parameter object (can be null)
     * @return Map of backend calls
     */
    abstract Map<CallType, Callable<BaseBodyDataModel>> callers(P contentRequestParameter);

    /**
     * Combines the results of the calls to a single returning object of type T.
     *
     * @param result {@link Map} of backend call results by their {@link CallType}
     * @return combined result of type T
     */
    abstract T combinator(Map<CallType, BaseBodyDataModel> result);

    private Map<CallType, BaseBodyDataModel> doCalls(P contentRequestParameter) {

        Map<CallType, Future<BaseBodyDataModel>> futureMap = callers(contentRequestParameter).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, callableEntry -> contentAdapterExecutor.submit(callableEntry.getValue())));

        return futureMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, callableEntry -> extractResult(callableEntry.getValue())));
    }

    private BaseBodyDataModel extractResult(Future<BaseBodyDataModel> dataModelFuture) {

        BaseBodyDataModel result = null;
        try {
            result = dataModelFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Failed to extract result from Future", e);
        }

        return result;
    }
}
