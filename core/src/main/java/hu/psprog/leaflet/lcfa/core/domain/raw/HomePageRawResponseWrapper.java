package hu.psprog.leaflet.lcfa.core.domain.raw;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import lombok.Builder;

/**
 * Wrapper domain class for holding raw responses used by home page rendering.
 *
 * @author Peter Smith
 */
@Builder
public record HomePageRawResponseWrapper(
        CategoryListDataModel categoryListDataModel,
        WrapperBodyDataModel<EntryListDataModel> wrappedEntryListDataModel,
        WrapperBodyDataModel<TagListDataModel> wrappedTagListDataModel
) { }
