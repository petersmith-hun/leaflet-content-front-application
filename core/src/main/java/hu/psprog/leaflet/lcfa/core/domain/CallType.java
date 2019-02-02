package hu.psprog.leaflet.lcfa.core.domain;

/**
 * Possible backend call types.
 * Can be used for differentiating async parallel backend calls.
 *
 * @author Peter Smith
 */
public enum CallType {

    CATEGORY,
    COMMENT,
    ENTRY,
    TAG
}
