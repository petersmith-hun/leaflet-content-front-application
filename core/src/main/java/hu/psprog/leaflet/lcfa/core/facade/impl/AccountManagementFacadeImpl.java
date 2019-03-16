package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.lcfa.core.converter.AccountBaseInfoConverter;
import hu.psprog.leaflet.lcfa.core.domain.account.AccountBaseInfo;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountDeletionRequest;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountRequestWrapper;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.AccountManagementFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import hu.psprog.leaflet.lcfa.core.facade.impl.utility.AccountDeletionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link AccountManagementFacade}.
 *
 * @author Peter Smith
 */
@Service
public class AccountManagementFacadeImpl implements AccountManagementFacade {

    private static final String BASE_ACCOUNT_INFO_RETRIEVAL_FAILED = "Failed to retrieve base account info for user [%d]";

    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private AccountBaseInfoConverter accountBaseInfoConverter;
    private AccountDeletionHandler accountDeletionHandler;

    @Autowired
    public AccountManagementFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry,
                                       AccountBaseInfoConverter accountBaseInfoConverter,
                                       AccountDeletionHandler accountDeletionHandler) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.accountBaseInfoConverter = accountBaseInfoConverter;
        this.accountDeletionHandler = accountDeletionHandler;
    }

    @Override
    public AccountBaseInfo getAccountBaseInfo(Long userID) {

        assertUserIsAuthenticated(userID);

        return getUserData(userID)
                .map(accountBaseInfoConverter::convert)
                .orElseThrow(() -> new UserRequestProcessingException(String.format(BASE_ACCOUNT_INFO_RETRIEVAL_FAILED, userID)));
    }

    @Override
    public boolean updateAccountBaseInfo(Long userID, UpdateProfileRequestModel updateProfileRequestModel) {

        assertUserIsAuthenticated(userID);

        return contentRequestAdapterRegistry.<ExtendedUserDataModel, AccountRequestWrapper<UpdateProfileRequestModel>>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_UPDATE)
                .getContent(new AccountRequestWrapper<>(userID, updateProfileRequestModel))
                .map(extendedUserDataModel -> isUpdateSuccessful(updateProfileRequestModel, extendedUserDataModel))
                .orElse(false);
    }

    @Override
    public boolean updatePassword(Long userID, PasswordChangeRequestModel passwordChangeRequestModel) {

        assertUserIsAuthenticated(userID);

        return contentRequestAdapterRegistry.<Boolean, AccountRequestWrapper<PasswordChangeRequestModel>>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_PASSWORD_CHANGE)
                .getContent(new AccountRequestWrapper<>(userID, passwordChangeRequestModel))
                .orElse(false);
    }

    @Override
    public boolean deleteAccount(Long userID, AccountDeletionRequest accountDeletionRequest) {
        return accountDeletionHandler.deleteAccount(userID, accountDeletionRequest);
    }

    private void assertUserIsAuthenticated(Long userID) {
        if (Objects.isNull(userID)) {
            throw new UserRequestProcessingException("User is not authenticated");
        }
    }

    private Optional<ExtendedUserDataModel> getUserData(Long userID) {
        return contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO)
                .getContent(userID);
    }

    private boolean isUpdateSuccessful(UpdateProfileRequestModel updateProfileRequestModel, ExtendedUserDataModel currentUserDataModel) {
        return updateProfileRequestModel.getEmail().equals(currentUserDataModel.getEmail())
                && updateProfileRequestModel.getUsername().equals(currentUserDataModel.getUsername());
    }
}
