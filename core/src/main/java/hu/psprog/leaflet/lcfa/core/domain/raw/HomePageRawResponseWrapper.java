package hu.psprog.leaflet.lcfa.core.domain.raw;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Wrapper domain class for holding raw responses used by home page rendering.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class HomePageRawResponseWrapper {

    private CategoryListDataModel categoryListDataModel;
    private WrapperBodyDataModel<EntryListDataModel> wrappedEntryListDataModel;
    private WrapperBodyDataModel<TagListDataModel> wrappedTagListDataModel;
}
