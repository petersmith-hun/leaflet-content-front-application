package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ConflictingRequestException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.converter.SignUpRequestConverter;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.SignUpResult;
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
public class SignUpRequestContentRequestAdapter implements ContentRequestAdapter<SignUpResult, SignUpRequestModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpRequestContentRequestAdapter.class);

    private UserBridgeService userBridgeService;
    private SignUpRequestConverter signUpRequestConverter;

    @Autowired
    public SignUpRequestContentRequestAdapter(UserBridgeService userBridgeService, SignUpRequestConverter signUpRequestConverter) {
        this.userBridgeService = userBridgeService;
        this.signUpRequestConverter = signUpRequestConverter;
    }

    @Override
    public Optional<SignUpResult> getContent(SignUpRequestModel contentRequestParameter) {

        UserInitializeRequestModel userInitializeRequestModel = signUpRequestConverter.convert(contentRequestParameter);
        SignUpResult signUpResult = SignUpResult.SUCCESS;
        try {
            userBridgeService.signUp(userInitializeRequestModel, contentRequestParameter.getRecaptchaToken());
        } catch (ConflictingRequestException e) {
            LOGGER.warn("Sign-up attempt with already used email address", e);
            signUpResult = SignUpResult.ADDRESS_IN_USE;
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to process sign-up request", e);
            signUpResult = SignUpResult.FAILURE;
        }

        return Optional.of(signUpResult);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.SIGN_UP;
    }
}
