package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry for {@link NavigationItemFactory} implementations.
 * Able to provide a factory based on the used source model class.
 *
 * @author Peter Smith
 */
@Component
public class NavigationItemFactoryRegistry {

    private static final String FACTORY_IMPLEMENTATION_NOT_FOUND = "NavigationItemFactory implementation not found for model class %s";

    private final Map<Class<?>, NavigationItemFactory<?>> registry;

    @Autowired
    public NavigationItemFactoryRegistry(List<NavigationItemFactory<?>> navigationItemFactoryList) {
        registry = navigationItemFactoryList.stream()
                .collect(Collectors.toMap(NavigationItemFactory::forModelClass, Function.identity()));
    }

    /**
     * Provides the {@link NavigationItemFactory} assigned to the given source model class.
     * Throws {@link IllegalArgumentException} in case no factory implementation is present in the registry for the given modell class.
     *
     * @param modelClass model class of which navigation item factory is needed
     * @param <T> type of the model class
     * @return assigned {@link NavigationItemFactory} implementation
     */
    public <T> NavigationItemFactory<T> getFactory(Class<T> modelClass) {
        return Optional.ofNullable(registry.get(modelClass))
                .map(navigationItemFactory -> (NavigationItemFactory<T>) navigationItemFactory)
                .orElseThrow(() -> new IllegalArgumentException(String.format(FACTORY_IMPLEMENTATION_NOT_FOUND, modelClass.getName())));
    }
}
