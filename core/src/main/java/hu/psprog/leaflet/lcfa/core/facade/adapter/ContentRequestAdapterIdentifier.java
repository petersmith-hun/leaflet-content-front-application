package hu.psprog.leaflet.lcfa.core.facade.adapter;

/**
 * Possible content request adapter identifiers.
 * Every {@link ContentRequestAdapter} implementation should have its own.
 *
 * @author Peter Smith
 */
public enum ContentRequestAdapterIdentifier {

    ARTICLE,
    CATEGORY_FILTER,
    COMMON_PAGE_DATA,
    CONTACT_REQUEST,
    CONTENT_FILTER,
    HOME_PAGE,
    SIGN_UP,
    STATIC_PAGE,
    TAG_FILTER
}
