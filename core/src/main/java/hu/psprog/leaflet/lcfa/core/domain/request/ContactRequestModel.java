package hu.psprog.leaflet.lcfa.core.domain.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Contact request model.
 *
 * @author Peter Smith
 */
@Data
public class ContactRequestModel {

    @NotEmpty
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String message;

    @NotEmpty
    private String recaptchaToken;
}
