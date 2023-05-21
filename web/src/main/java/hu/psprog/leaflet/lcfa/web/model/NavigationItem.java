package hu.psprog.leaflet.lcfa.web.model;

import lombok.Data;

/**
 * Model class for navigation bar items.
 *
 * @author Peter Smith
 */
@Data
public class NavigationItem {

    private final String link;
    private final String title;
    private final boolean translatable;

    private NavigationItem(String link, String title, boolean translatable) {
        this.link = link;
        this.title = title;
        this.translatable = translatable;
    }

    /**
     * Creates a non-translatable {@link NavigationItem} object.
     *
     * @param link link of the item
     * @param title title of the item
     * @return built {@link NavigationItem} object
     */
    public static NavigationItem build(String link, String title) {
        return new NavigationItem(link, title, false);
    }

    /**
     * Creates a translatable {@link NavigationItem} object.
     *
     * @param link link of the item
     * @param tag i18n tag
     * @return built {@link NavigationItem} object
     */
    public static NavigationItem buildTranslatable(String link, String tag) {
        return new NavigationItem(link, tag, true);
    }

}
