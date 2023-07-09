package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.domain.common.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link EntryListDataModel} wrapped in {@link WrapperBodyDataModel} to {@link CommonPageData}.
 *
 * @author Peter Smith
 */
@Component
public class CommonPageDataConverter implements Converter<WrapperBodyDataModel<EntryListDataModel>, CommonPageData> {

    private static final String LOGIN_ROUTE_ID = "LOGIN";

    private final WrappedDataExtractor wrappedDataExtractor;
    private final EntrySummaryListConverter entrySummaryListConverter;

    @Autowired
    public CommonPageDataConverter(WrappedDataExtractor wrappedDataExtractor, EntrySummaryListConverter entrySummaryListConverter) {
        this.wrappedDataExtractor = wrappedDataExtractor;
        this.entrySummaryListConverter = entrySummaryListConverter;
    }

    @Override
    public CommonPageData convert(WrapperBodyDataModel<EntryListDataModel> source) {

        List<MenuItem> headerMenu = wrappedDataExtractor.extractHeaderMenu(source);

        return CommonPageData.builder()
                .seo(wrappedDataExtractor.extractSEOAttributes(source))
                .headerMenu(headerMenu)
                .footerMenu(wrappedDataExtractor.extractFooterMenu(source))
                .standaloneMenuItems(wrappedDataExtractor.extractStandaloneMenuItems(source))
                .latestEntries(entrySummaryListConverter.convert(source.body()))
                .loginMenuItem(getLoginMenuItem(headerMenu))
                .build();
    }

    private MenuItem getLoginMenuItem(List<MenuItem> headerMenu) {

        return headerMenu.stream()
                .filter(menuItem -> menuItem.routeId().equals(LOGIN_ROUTE_ID))
                .findFirst()
                .orElse(null);
    }
}
