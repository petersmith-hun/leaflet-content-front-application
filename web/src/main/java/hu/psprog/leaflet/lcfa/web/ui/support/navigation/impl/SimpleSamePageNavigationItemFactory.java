package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import org.springframework.stereotype.Component;

/**
 * {@link NavigationItemFactory} implementation for generating navigation bar items with same-page links.
 *
 * @author Peter Smith
 */
@Component
public class SimpleSamePageNavigationItemFactory implements NavigationItemFactory<String> {

    private static final String SAME_PAGE_LINK = "''";

    @Override
    public NavigationItem create(String sourceModel) {
        return NavigationItem.build(SAME_PAGE_LINK, sourceModel);
    }

    @Override
    public Class<String> forModelClass() {
        return String.class;
    }
}
