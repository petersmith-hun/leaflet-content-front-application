package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.routing.FrontEndRouteDataModel;
import hu.psprog.leaflet.lcfa.core.domain.common.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.lcfa.core.domain.common.MenuItem;
import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Extraction utility component for {@link WrapperBodyDataModel} data objects.
 *
 * @author Peter Smith
 */
@Component
public class WrappedDataExtractor {

    private static final PaginationAttributes DEFAULT_PAGINATION_ATTRIBUTES = PaginationAttributes.builder()
            .pageNumber(1)
            .pageCount(1)
            .hasPrevious(false)
            .hasNext(false)
            .build();
    private static final SEOAttributes DEFAULT_SEO_ATTRIBUTES = SEOAttributes.builder().build();

    /**
     * Extracts pagination information from the given {@link WrapperBodyDataModel}.
     *
     * @param wrapperBodyDataModel {@link WrapperBodyDataModel} container object
     * @return extracted {@link PaginationAttributes} or instantiated empty object if not present
     */
    public PaginationAttributes extractPaginationAttributes(WrapperBodyDataModel<?> wrapperBodyDataModel) {
        return Optional.ofNullable(wrapperBodyDataModel.pagination())
                .map(pagination -> PaginationAttributes.builder()
                        .hasNext(pagination.hasNext())
                        .hasPrevious(pagination.hasPrevious())
                        .pageCount(pagination.pageCount())
                        .pageNumber(pagination.pageNumber())
                        .build())
                .orElse(DEFAULT_PAGINATION_ATTRIBUTES);
    }

    /**
     * Extracts SEO information from the given {@link WrapperBodyDataModel}.
     *
     * @param wrapperBodyDataModel {@link WrapperBodyDataModel} container object
     * @return extracted {@link SEOAttributes} or instantiated empty object if not present
     */
    public SEOAttributes extractSEOAttributes(WrapperBodyDataModel<?> wrapperBodyDataModel) {
        return Optional.ofNullable(wrapperBodyDataModel.seo())
                .map(seo -> SEOAttributes.builder()
                        .pageTitle(seo.pageTitle())
                        .metaTitle(seo.metaTitle())
                        .metaDescription(seo.metaDescription())
                        .metaKeywords(seo.metaKeywords())
                        .build())
                .orElse(DEFAULT_SEO_ATTRIBUTES);
    }

    /**
     * Extracts header menu from the given {@link WrapperBodyDataModel}.
     *
     * @param wrapperBodyDataModel {@link WrapperBodyDataModel} container object
     * @return extracted {@link List} of {@link MenuItem} objects or {@code null} if not present
     */
    public List<MenuItem> extractHeaderMenu(WrapperBodyDataModel<?> wrapperBodyDataModel) {
        return extractMenu(wrapperBodyDataModel.menu().header());
    }

    /**
     * Extracts footer menu from the given {@link WrapperBodyDataModel}.
     *
     * @param wrapperBodyDataModel {@link WrapperBodyDataModel} container object
     * @return extracted {@link List} of {@link MenuItem} objects or {@code null} if not present
     */
    public List<MenuItem> extractFooterMenu(WrapperBodyDataModel<?> wrapperBodyDataModel) {
        return extractMenu(wrapperBodyDataModel.menu().footer());
    }

    /**
     * Extracts standalone menu items from the given {@link WrapperBodyDataModel}.
     *
     * @param wrapperBodyDataModel {@link WrapperBodyDataModel} container object
     * @return extracted {@link List} of {@link MenuItem} objects or {@code null} if not present
     */
    public List<MenuItem> extractStandaloneMenuItems(WrapperBodyDataModel<?> wrapperBodyDataModel) {
        return extractMenu(wrapperBodyDataModel.menu().standalone());
    }

    private List<MenuItem> extractMenu(List<FrontEndRouteDataModel> menuItemList) {
        return Optional.ofNullable(menuItemList)
                .map(menuItems -> menuItems.stream()
                        .map(mapMenuItem())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private Function<FrontEndRouteDataModel, MenuItem> mapMenuItem() {
        return frontEndRouteDataModel -> MenuItem.builder()
                .name(frontEndRouteDataModel.name())
                .routeId(frontEndRouteDataModel.routeId())
                .url(frontEndRouteDataModel.url())
                .authRequirement(FrontEndRouteAuthRequirement.valueOf(frontEndRouteDataModel.authRequirement()))
                .build();
    }
}
