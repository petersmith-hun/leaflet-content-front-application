package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
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
 * {@link ContentRequestAdapter} implementation for handling user password change requests.
 *
 * @author Peter Smith
 */
@Component
public class UserPasswordChangeContentRequestAdapter implements ContentRequestAdapter<Boolean, AccountRequestWrapper<PasswordChangeRequestModel>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPasswordChangeContentRequestAdapter.class);

    private UserBridgeService userBridgeService;

    @Autowired
    public UserPasswordChangeContentRequestAdapter(UserBridgeService userBridgeService) {
        this.userBridgeService = userBridgeService;
    }

    @Override
    public Optional<Boolean> getContent(AccountRequestWrapper<PasswordChangeRequestModel> contentRequestParameter) {

        Boolean successful = null;
        try {
            userBridgeService.updatePassword(contentRequestParameter.getCurrentUserID(), contentRequestParameter.getRequestPayload());
            successful = true;
        } catch (UnauthorizedAccessException e) {
            throw new UserSessionInvalidationRequiredException(e);
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to update user password", e);
        }

        return Optional.ofNullable(successful);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.PROFILE_PASSWORD_CHANGE;
    }
}
