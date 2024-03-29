package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.lcfa.core.config.DefaultPaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.account.AccountBaseInfo;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountRequestWrapper;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.AccountManagementFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import hu.psprog.leaflet.lcfa.core.facade.impl.utility.AccountDeletionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
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

    private final ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private final AccountDeletionHandler accountDeletionHandler;
    private final ConversionService conversionService;
    private final DefaultPaginationAttributes<OrderBy.Comment> defaultPaginationAttributes;

    @Autowired
    public AccountManagementFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry, AccountDeletionHandler accountDeletionHandler,
                                       ConversionService conversionService, DefaultPaginationAttributes<OrderBy.Comment> defaultPaginationAttributes) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.accountDeletionHandler = accountDeletionHandler;
        this.conversionService = conversionService;
        this.defaultPaginationAttributes = defaultPaginationAttributes;
    }

    @Override
    public AccountBaseInfo getAccountBaseInfo(Long userID) {

        assertUserIsAuthenticated(userID);

        return getUserData(userID)
                .map(extendedUserDataModel -> conversionService.convert(extendedUserDataModel, AccountBaseInfo.class))
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
    public UserCommentsPageContent getCommentsForUser(Long userID, int page) {

        assertUserIsAuthenticated(userID);

        return contentRequestAdapterRegistry.<WrapperBodyDataModel<ExtendedCommentListDataModel>, FilteredPaginationContentRequest<Long, OrderBy.Comment>>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENTS_OF_USER)
                .getContent(createFilteredRequest(userID, page))
                .map(response -> conversionService.convert(response, UserCommentsPageContent.class))
                .orElse(UserCommentsPageContent.EMPTY_CONTENT);
    }

    @Override
    public boolean deleteAccount(Long userID) {
        return accountDeletionHandler.deleteAccount(userID);
    }

    @Override
    public boolean deleteComment(Long commentID) {
        return contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_DELETION)
                .getContent(commentID)
                .orElse(false);
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
        return updateProfileRequestModel.getEmail().equals(currentUserDataModel.email())
                && updateProfileRequestModel.getUsername().equals(currentUserDataModel.username());
    }

    private FilteredPaginationContentRequest<Long, OrderBy.Comment> createFilteredRequest(Long userID, int page) {
        return FilteredPaginationContentRequest.<Long, OrderBy.Comment>builder()
                .filterValue(userID)
                .page(page)
                .limit(defaultPaginationAttributes.getLimit())
                .orderBy(defaultPaginationAttributes.getOrderBy())
                .orderDirection(defaultPaginationAttributes.getOrderDirection())
                .build();
    }
}
