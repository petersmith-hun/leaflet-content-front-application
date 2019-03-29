package hu.psprog.leaflet.lcfa.web.ui.support.navigation;

import hu.psprog.leaflet.lcfa.web.model.NavigationItem;

/**
 * Factory interface for generating navigation bar items.
 *
 * @param <T> type of the source model from which the navigation item can be generated
 * @author Peter Smith
 */
public interface NavigationItemFactory<T> {

    /**
     * Creates a navigation item from the given source model.
     *
     * @param sourceModel source model of type T
     * @return generated {@link NavigationItem} object
     */
    NavigationItem create(T sourceModel);

    /**
     * Defines source model type for which a specific factory implementation can be used.
     *
     * @return source model type as {@link Class}
     */
    Class<T> forModelClass();
}
