package hu.psprog.leaflet.lcfa.web.model;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;

/**
 * Known standard model fields (except for common page data fields) with the actual field names.
 *
 * @see CommonPageDataField
 * @author Peter Smith
 */
public enum ModelField {

    LIST_CATEGORIES("categories"),
    LIST_ENTRIES("entries"),
    LIST_TAGS("tags"),

    ARTICLE("article"),
    COMMENTS("comments"),
    CURRENT_PAGE_NUMBER("currentPageNumber"),
    LINK_TEMPLATE("linkTemplate"),
    PAGINATION("pagination"),
    STATIC("static");

    private String fieldName;

    ModelField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
