package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.converter.SignUpRequestConverter;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation to process sign-up requests.
 *
 * @author Peter Smith
 */
@Component
public class SignUpRequestContentRequestAdapter implements ContentRequestAdapter<Boolean, SignUpRequestModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpRequestContentRequestAdapter.class);

    private UserBridgeService userBridgeService;
    private SignUpRequestConverter signUpRequestConverter;

    @Autowired
    public SignUpRequestContentRequestAdapter(UserBridgeService userBridgeService, SignUpRequestConverter signUpRequestConverter) {
        this.userBridgeService = userBridgeService;
        this.signUpRequestConverter = signUpRequestConverter;
    }

    @Override
    public Optional<Boolean> getContent(SignUpRequestModel contentRequestParameter) {

        UserInitializeRequestModel userInitializeRequestModel = signUpRequestConverter.convert(contentRequestParameter);
        Boolean success = null;
        try {
            userBridgeService.signUp(userInitializeRequestModel, contentRequestParameter.getRecaptchaToken());
            success = true;
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to process sign-up request", e);
        }

        return Optional.ofNullable(success);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.SIGN_UP;
    }
}
