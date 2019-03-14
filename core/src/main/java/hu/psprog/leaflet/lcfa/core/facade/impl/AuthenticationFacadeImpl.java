package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.jwt.auth.support.service.impl.utility.AuthenticationUtility;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordReclaimRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordResetConfirmationRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.PasswordResetResult;
import hu.psprog.leaflet.lcfa.core.domain.result.SignUpResult;
import hu.psprog.leaflet.lcfa.core.facade.AuthenticationFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AuthenticationFacade}.
 *
 * @author Peter Smith
 */
@Service
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private AuthenticationUtility authenticationUtility;

    @Autowired
    public AuthenticationFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry, AuthenticationUtility authenticationUtility) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.authenticationUtility = authenticationUtility;
    }

    @Override
    public SignUpResult signUp(SignUpRequestModel signUpRequestModel) {
        return contentRequestAdapterRegistry.<SignUpResult, SignUpRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.SIGN_UP)
                .getContent(signUpRequestModel)
                .orElse(SignUpResult.FAILURE);
    }

    @Override
    public PasswordResetResult requestPasswordReset(PasswordReclaimRequestModel passwordReclaimRequestModel) {
        return contentRequestAdapterRegistry.<PasswordResetResult, PasswordReclaimRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.PASSWORD_RESET_REQUEST)
                .getContent(passwordReclaimRequestModel)
                .orElse(PasswordResetResult.DEMAND_FAILED);
    }

    @Override
    public PasswordResetResult confirmPasswordReset(PasswordResetConfirmationRequestModel passwordResetConfirmationRequestModel, String reclaimToken) {
        authenticationUtility.createAndStoreTemporal(reclaimToken);
        return contentRequestAdapterRegistry.<PasswordResetResult, PasswordResetConfirmationRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.PASSWORD_RESET_CONFIRMATION)
                .getContent(passwordResetConfirmationRequestModel)
                .orElse(PasswordResetResult.CONFIRMATION_FAILED);
    }
}
