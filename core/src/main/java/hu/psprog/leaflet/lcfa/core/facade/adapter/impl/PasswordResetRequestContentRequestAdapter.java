package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.user.PasswordResetDemandRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordReclaimRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.PasswordResetResult;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation for handling password reset requests.
 *
 * @author Peter Smith
 */
@Component
public class PasswordResetRequestContentRequestAdapter implements ContentRequestAdapter<PasswordResetResult, PasswordReclaimRequestModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetRequestContentRequestAdapter.class);

    private UserBridgeService userBridgeService;

    @Autowired
    public PasswordResetRequestContentRequestAdapter(UserBridgeService userBridgeService) {
        this.userBridgeService = userBridgeService;
    }

    @Override
    public Optional<PasswordResetResult> getContent(PasswordReclaimRequestModel contentRequestParameter) {

        PasswordResetResult result = PasswordResetResult.DEMAND_FAILED;
        try {
            userBridgeService.demandPasswordReset(convert(contentRequestParameter), contentRequestParameter.getRecaptchaToken());
            result = PasswordResetResult.DEMAND_PROCESSED;
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to process password reset request", e);
        }

        return Optional.of(result);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.PASSWORD_RESET_REQUEST;
    }

    private PasswordResetDemandRequestModel convert(PasswordReclaimRequestModel passwordReclaimRequestModel) {

        PasswordResetDemandRequestModel passwordResetDemandRequestModel = new PasswordResetDemandRequestModel();
        passwordResetDemandRequestModel.setEmail(passwordReclaimRequestModel.getEmail());

        return passwordResetDemandRequestModel;
    }
}
