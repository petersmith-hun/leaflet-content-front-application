package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation for handling user profile base info requests.
 *
 * @author Peter Smith
 */
@Component
public class UserDataContentRequestAdapter implements ContentRequestAdapter<ExtendedUserDataModel, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataContentRequestAdapter.class);

    private UserBridgeService userBridgeService;

    @Autowired
    public UserDataContentRequestAdapter(UserBridgeService userBridgeService) {
        this.userBridgeService = userBridgeService;
    }

    @Override
    public Optional<ExtendedUserDataModel> getContent(Long contentRequestParameter) {

        ExtendedUserDataModel userDataModel = null;
        try {
            userDataModel = userBridgeService.getUserByID(contentRequestParameter);
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to retrieve user data for user [{}]", contentRequestParameter, e);
        }

        return Optional.ofNullable(userDataModel);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.PROFILE_BASE_INFO;
    }
}
