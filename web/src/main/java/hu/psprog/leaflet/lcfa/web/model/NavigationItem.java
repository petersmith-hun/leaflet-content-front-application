package hu.psprog.leaflet.lcfa.web.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Model class for navigation bar items.
 *
 * @author Peter Smith
 */
public class NavigationItem {

    private String link;
    private String title;
    private boolean translatable;

    private NavigationItem(String link, String title, boolean translatable) {
        this.link = link;
        this.title = title;
        this.translatable = translatable;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public boolean isTranslatable() {
        return translatable;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NavigationItem that = (NavigationItem) o;

        return new EqualsBuilder()
                .append(link, that.link)
                .append(title, that.title)
                .append(translatable, that.translatable)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(link)
                .append(title)
                .append(translatable)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("link", link)
                .append("title", title)
                .append("translatable", translatable)
                .toString();
    }
}
