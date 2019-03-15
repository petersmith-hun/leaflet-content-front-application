package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation for handling account deletion requests.
 *
 * @author Peter Smith
 */
@Component
public class UserAccountDeletionContentRequestAdapter implements ContentRequestAdapter<Boolean, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountDeletionContentRequestAdapter.class);
    private static final String FAILED_TO_DELETE_USER_ACCOUNT = "Failed to delete user account for user [{}]";

    private UserBridgeService userBridgeService;

    @Autowired
    public UserAccountDeletionContentRequestAdapter(UserBridgeService userBridgeService) {
        this.userBridgeService = userBridgeService;
    }

    @Override
    public Optional<Boolean> getContent(Long contentRequestParameter) {

        Boolean successful = null;
        try {
            userBridgeService.deleteUser(contentRequestParameter);
            successful = true;
        } catch (CommunicationFailureException e) {
            LOGGER.error(FAILED_TO_DELETE_USER_ACCOUNT, contentRequestParameter, e);
        }

        return Optional.ofNullable(successful);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.PROFILE_DELETE;
    }
}
