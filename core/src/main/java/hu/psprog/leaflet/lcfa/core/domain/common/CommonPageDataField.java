package hu.psprog.leaflet.lcfa.core.domain.common;

import java.util.function.Function;

/**
 * Common page data field definitions.
 * Declares the actual field name, and the extraction function to retrieve field content from {@link CommonPageData}.
 *
 * @author Peter Smith
 */
public enum CommonPageDataField {

    HEADER_MENU("headerMenu", CommonPageData::getHeaderMenu),
    FOOTER_MENU("footerMenu", CommonPageData::getFooterMenu),
    STANDALONE_MENU_ITEMS("standaloneMenuItems", CommonPageData::getStandaloneMenuItems),
    LATEST_ENTRIES("latest", CommonPageData::getLatestEntries),
    SEO_ATTRIBUTES("seo", CommonPageData::getSeo);

    private String fieldName;
    private Function<CommonPageData, Object> mapperFunction;

    CommonPageDataField(String fieldName, Function<CommonPageData, Object> mapperFunction) {
        this.fieldName = fieldName;
        this.mapperFunction = mapperFunction;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Function<CommonPageData, Object> getMapperFunction() {
        return mapperFunction;
    }
}
