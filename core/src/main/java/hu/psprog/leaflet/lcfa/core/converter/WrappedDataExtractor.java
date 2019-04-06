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
        return Optional.ofNullable(wrapperBodyDataModel.getPagination())
                .map(pagination -> PaginationAttributes.builder()
                        .hasNext(pagination.isHasNext())
                        .hasPrevious(pagination.isHasPrevious())
                        .pageCount(pagination.getPageCount())
                        .pageNumber(pagination.getPageNumber())
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
        return Optional.ofNullable(wrapperBodyDataModel.getSeo())
                .map(seo -> SEOAttributes.builder()
                        .pageTitle(seo.getPageTitle())
                        .metaTitle(seo.getMetaTitle())
                        .metaDescription(seo.getMetaDescription())
                        .metaKeywords(seo.getMetaKeywords())
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
        return extractMenu(wrapperBodyDataModel.getMenu().getHeader());
    }

    /**
     * Extracts footer menu from the given {@link WrapperBodyDataModel}.
     *
     * @param wrapperBodyDataModel {@link WrapperBodyDataModel} container object
     * @return extracted {@link List} of {@link MenuItem} objects or {@code null} if not present
     */
    public List<MenuItem> extractFooterMenu(WrapperBodyDataModel<?> wrapperBodyDataModel) {
        return extractMenu(wrapperBodyDataModel.getMenu().getFooter());
    }

    /**
     * Extracts standalone menu items from the given {@link WrapperBodyDataModel}.
     *
     * @param wrapperBodyDataModel {@link WrapperBodyDataModel} container object
     * @return extracted {@link List} of {@link MenuItem} objects or {@code null} if not present
     */
    public List<MenuItem> extractStandaloneMenuItems(WrapperBodyDataModel<?> wrapperBodyDataModel) {
        return extractMenu(wrapperBodyDataModel.getMenu().getStandalone());
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
                .name(frontEndRouteDataModel.getName())
                .routeId(frontEndRouteDataModel.getRouteId())
                .url(frontEndRouteDataModel.getUrl())
                .authRequirement(FrontEndRouteAuthRequirement.valueOf(frontEndRouteDataModel.getAuthRequirement()))
                .build();
    }
}
