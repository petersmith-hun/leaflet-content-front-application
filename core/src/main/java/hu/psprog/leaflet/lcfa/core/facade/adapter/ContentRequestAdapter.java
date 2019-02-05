package hu.psprog.leaflet.lcfa.core.facade.adapter;

import java.util.Optional;

/**
 * Implementations wrap the logic of retrieving content from the backend.
 *
 * @param <T> return type
 * @param <P> additional request parameter type (can be Void as well)
 * @author Peter Smith
 */
public interface ContentRequestAdapter<T, P> {

    /**
     * Retrieves content returned by the backend.
     *
     * @param contentRequestParameter additional request parameter object
     * @return returned (processed) response wrapped as {@link Optional}
     */
    Optional<T> getContent(P contentRequestParameter);

    /**
     * Returns the assigned identifier.
     *
     * @return identifier as {@link ContentRequestAdapterIdentifier} enum constant
     */
    ContentRequestAdapterIdentifier getIdentifier();
}
