package hu.psprog.leaflet.lcfa.core.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Account deletion request model class.
 *
 * @author Peter Smith
 */
@Setter
@Getter
public class AccountDeletionRequest {

    private String password;
}
