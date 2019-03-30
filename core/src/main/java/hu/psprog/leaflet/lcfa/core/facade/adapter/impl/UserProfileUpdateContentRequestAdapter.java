package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountRequestWrapper;
import hu.psprog.leaflet.lcfa.core.exception.UserSessionInvalidationRequiredException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation for handling user profile update requests.
 *
 * @author Peter Smith
 */
@Component
public class UserProfileUpdateContentRequestAdapter implements ContentRequestAdapter<ExtendedUserDataModel, AccountRequestWrapper<UpdateProfileRequestModel>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileUpdateContentRequestAdapter.class);

    private UserBridgeService userBridgeService;

    @Autowired
    public UserProfileUpdateContentRequestAdapter(UserBridgeService userBridgeService) {
        this.userBridgeService = userBridgeService;
    }

    @Override
    public Optional<ExtendedUserDataModel> getContent(AccountRequestWrapper<UpdateProfileRequestModel> contentRequestParameter) {

        ExtendedUserDataModel userDataModel = null;
        try {
            userDataModel = userBridgeService.updateProfile(contentRequestParameter.getCurrentUserID(), contentRequestParameter.getRequestPayload());
        } catch (UnauthorizedAccessException e) {
            throw new UserSessionInvalidationRequiredException(e);
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to update user profile", e);
        }

        return Optional.ofNullable(userDataModel);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.PROFILE_UPDATE;
    }
}
