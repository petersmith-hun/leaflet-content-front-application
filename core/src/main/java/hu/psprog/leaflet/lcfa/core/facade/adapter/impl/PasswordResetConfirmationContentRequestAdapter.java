package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordResetConfirmationRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.PasswordResetResult;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation for handling password reset confirmation.
 *
 * @author Peter Smith
 */
@Component
public class PasswordResetConfirmationContentRequestAdapter implements ContentRequestAdapter<PasswordResetResult, PasswordResetConfirmationRequestModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetConfirmationContentRequestAdapter.class);

    private UserBridgeService userBridgeService;

    @Autowired
    public PasswordResetConfirmationContentRequestAdapter(UserBridgeService userBridgeService) {
        this.userBridgeService = userBridgeService;
    }

    @Override
    public Optional<PasswordResetResult> getContent(PasswordResetConfirmationRequestModel contentRequestParameter) {

        PasswordResetResult result = PasswordResetResult.CONFIRMATION_FAILED;
        try {
            userBridgeService.confirmPasswordReset(contentRequestParameter, contentRequestParameter.getRecaptchaToken());
            result = PasswordResetResult.CONFIRMATION_PROCESSED;
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Password reset confirmation could not be processed", e);
        }

        return Optional.of(result);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.PASSWORD_RESET_CONFIRMATION;
    }
}
