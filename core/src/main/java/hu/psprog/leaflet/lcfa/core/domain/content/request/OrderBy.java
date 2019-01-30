package hu.psprog.leaflet.lcfa.core.domain.content.request;

/**
 * Available ordering fields.
 *
 * @author Peter Smith
 */
public class OrderBy {

    /**
     * Available ordering options for entries.
     */
    public enum Entry {
        ID,
        TITLE,
        CREATED
    }

    /**
     * Available ordering options for comments.
     */
    public enum Comment {
        ID,
        CREATED;
    }

    private OrderBy() {
    }
}
