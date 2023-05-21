package hu.psprog.leaflet.lcfa.core.domain.account;

import lombok.Builder;

/**
 * Account base info domain class.
 *
 * @author Peter Smith
 */
@Builder
public record AccountBaseInfo(
        String username,
        String email,
        String locale
) { }
