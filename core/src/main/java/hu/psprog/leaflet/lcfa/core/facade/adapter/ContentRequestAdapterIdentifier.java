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
    COMMENT_DELETION,
    COMMENT_POST,
    COMMENTS_OF_USER,
    COMMON_PAGE_DATA,
    CONTACT_REQUEST,
    CONTENT_FILTER,
    HOME_PAGE,
    PROFILE_BASE_INFO,
    PROFILE_DELETE,
    PROFILE_PASSWORD_CHANGE,
    PROFILE_UPDATE,
    SITEMAP,
    STATIC_PAGE,
    TAG_FILTER,
    SYSTEM_STARTUP_NOTIFICATION
}
