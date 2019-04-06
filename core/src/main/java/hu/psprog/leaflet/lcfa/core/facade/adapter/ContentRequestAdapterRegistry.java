package hu.psprog.leaflet.lcfa.core.facade.adapter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registers all the available {@link ContentRequestAdapter} implementations, and creates a queryable map
 * by their assigned {@link ContentRequestAdapterIdentifier}.
 *
 * @author Peter Smith
 */
@Component
public class ContentRequestAdapterRegistry implements InitializingBean {

    private static final String NO_REQUEST_ADAPTER_IS_ASSIGNED_TO_TYPE = "No request adapter is assigned to type [%s]";
    private static final String DEFICIENT_CONTENT_REQUEST_ADAPTER_MAPPING = "Deficient content request adapter mapping - stopping context build";

    private Map<ContentRequestAdapterIdentifier, ContentRequestAdapter<?, ?>> contentRequestAdapterMap;

    @Autowired
    public ContentRequestAdapterRegistry(List<ContentRequestAdapter<?, ?>> contentRequestAdapterList) {
        contentRequestAdapterMap = contentRequestAdapterList.stream()
                .filter(contentRequestAdapter -> Objects.nonNull(contentRequestAdapter.getIdentifier()))
                .collect(Collectors.toMap(ContentRequestAdapter::getIdentifier, Function.identity()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assertMappings();
    }

    /**
     * Returns the {@link ContentRequestAdapter} implementation assigned to the given {@link ContentRequestAdapterIdentifier}.
     *
     * @param identifier {@link ContentRequestAdapterIdentifier} value identifying a {@link ContentRequestAdapter} implementation
     * @param <T> return type of the {@link ContentRequestAdapter}
     * @param <P> parameter type of the {@link ContentRequestAdapter}
     * @return resolved {@link ContentRequestAdapter} implementation
     */
    public <T, P> ContentRequestAdapter<T, P> getContentRequestAdapter(ContentRequestAdapterIdentifier identifier) {
        return Optional.ofNullable(contentRequestAdapterMap.get(identifier))
                .map(contentRequestAdapter -> (ContentRequestAdapter<T, P>) contentRequestAdapter)
                .orElseThrow(() -> new IllegalArgumentException(String.format(NO_REQUEST_ADAPTER_IS_ASSIGNED_TO_TYPE, identifier)));
    }

    private void assertMappings() {
        if (contentRequestAdapterMap.keySet().size() != ContentRequestAdapterIdentifier.values().length) {
            throw new IllegalStateException(DEFICIENT_CONTENT_REQUEST_ADAPTER_MAPPING);
        }
    }
}
