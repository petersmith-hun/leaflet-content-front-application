package hu.psprog.leaflet.lcfa.core.domain.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Wrapper class for account operation requests.
 * Contains an actual (generic) payload and the authenticated user's ID.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class AccountRequestWrapper<T> {

    private Long currentUserID;
    private T requestPayload;
}
