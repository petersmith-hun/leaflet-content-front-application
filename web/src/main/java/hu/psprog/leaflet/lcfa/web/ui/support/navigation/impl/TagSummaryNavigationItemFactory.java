package hu.psprog.leaflet.lcfa.web.ui.support.navigation.impl;

import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import hu.psprog.leaflet.lcfa.web.controller.BaseController;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.NavigationItemFactory;
import org.springframework.stereotype.Component;

/**
 * {@link NavigationItemFactory} implementation for generating navigation bar items based on {@link TagSummary} objects.
 *
 * @author Peter Smith
 */
@Component
public class TagSummaryNavigationItemFactory implements NavigationItemFactory<TagSummary> {

    private static final String TAG_ID = "{tagID}";
    private static final String TAG_ALIAS = "{tagAlias}";

    @Override
    public NavigationItem create(TagSummary sourceModel) {
        return NavigationItem.build(createLink(sourceModel), sourceModel.getName());
    }

    @Override
    public Class<TagSummary> forModelClass() {
        return TagSummary.class;
    }

    private String createLink(TagSummary tagSummary) {
        return BaseController.PATH_FILTER_BY_TAG
                .replace(TAG_ID, String.valueOf(tagSummary.getId()))
                .replace(TAG_ALIAS, tagSummary.getAlias());
    }
}
