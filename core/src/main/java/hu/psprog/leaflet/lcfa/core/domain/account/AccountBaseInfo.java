package hu.psprog.leaflet.lcfa.core.domain.account;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Account base info domain class.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class AccountBaseInfo {

    private String username;
    private String email;
    private String locale;
}
