package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.lcfa.core.config.DefaultPaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.account.AccountBaseInfo;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountDeletionRequest;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountRequestWrapper;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import hu.psprog.leaflet.lcfa.core.facade.impl.utility.AccountDeletionHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link AccountManagementFacadeImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountManagementFacadeImplTest {

    private static final long USER_ID = 1L;
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final UpdateProfileRequestModel UPDATE_PROFILE_REQUEST_MODEL = new UpdateProfileRequestModel();
    private static final AccountRequestWrapper<UpdateProfileRequestModel> PROFILE_UPDATE_ACCOUNT_REQUEST_WRAPPER = new AccountRequestWrapper<>(USER_ID, UPDATE_PROFILE_REQUEST_MODEL);
    private static final PasswordChangeRequestModel PASSWORD_CHANGE_REQUEST_MODEL = new PasswordChangeRequestModel();
    private static final AccountRequestWrapper<PasswordChangeRequestModel> PASSWORD_UPDATE_ACCOUNT_REQUEST_WRAPPER = new AccountRequestWrapper<>(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);
    private static final int PAGE_NUMBER = 3;
    private static final int ITEM_LIMIT_ON_PAGE = 10;
    private static final OrderBy.Comment ORDER_BY = OrderBy.Comment.CREATED;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.ASC;
    private static final AccountDeletionRequest ACCOUNT_DELETION_REQUEST = new AccountDeletionRequest();
    private static final long COMMENT_ID = 8L;
    private static final String CONTENT = "comment";
    private static final ExtendedUserDataModel EXTENDED_USER_DATA_MODEL = ExtendedUserDataModel.getExtendedBuilder()
            .withId(USER_ID)
            .withEmail(EMAIL)
            .withUsername(USERNAME)
            .build();
    private static final AccountBaseInfo ACCOUNT_BASE_INFO = AccountBaseInfo.builder()
            .email(EMAIL)
            .username(USERNAME)
            .build();
    private static final FilteredPaginationContentRequest<Long, OrderBy.Comment> FILTERED_PAGINATION_CONTENT_REQUEST = FilteredPaginationContentRequest.<Long, OrderBy.Comment>builder()
            .filterValue(USER_ID)
            .page(PAGE_NUMBER)
            .limit(ITEM_LIMIT_ON_PAGE)
            .orderBy(ORDER_BY)
            .orderDirection(ORDER_DIRECTION)
            .build();
    private static final WrapperBodyDataModel<ExtendedCommentListDataModel> WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(ExtendedCommentListDataModel.getBuilder()
                    .withItem(ExtendedCommentDataModel.getExtendedBuilder().withContent(CONTENT).build())
                    .build())
            .build();
    private static final UserCommentsPageContent USER_COMMENTS_PAGE_CONTENT = UserCommentsPageContent.builder()
            .comments(Collections.singletonList(CommentSummary.builder().content(CONTENT).build()))
            .build();

    static {
        UPDATE_PROFILE_REQUEST_MODEL.setEmail(EMAIL);
        UPDATE_PROFILE_REQUEST_MODEL.setUsername(USERNAME);
        PASSWORD_CHANGE_REQUEST_MODEL.setPassword("new password");
    }

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private AccountDeletionHandler accountDeletionHandler;

    @Mock
    private ConversionService conversionService;

    @Mock
    private DefaultPaginationAttributes<OrderBy.Comment> defaultPaginationAttributes;

    @Mock
    private ContentRequestAdapter<ExtendedUserDataModel, Long> profileBaseInfoContentRequestAdapter;
    
    @Mock
    private ContentRequestAdapter<ExtendedUserDataModel, AccountRequestWrapper<UpdateProfileRequestModel>> profileUpdateContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<Boolean, AccountRequestWrapper<PasswordChangeRequestModel>> passwordUpdateContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<WrapperBodyDataModel<ExtendedCommentListDataModel>, FilteredPaginationContentRequest<Long, OrderBy.Comment>> userCommentsContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<Boolean, Long> accountDeletionContentRequestAdapter;

    @InjectMocks
    private AccountManagementFacadeImpl accountManagementFacade;

    @Test
    public void shouldGetAccountBaseInfoReturnWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(profileBaseInfoContentRequestAdapter);
        given(profileBaseInfoContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.of(EXTENDED_USER_DATA_MODEL));
        given(conversionService.convert(EXTENDED_USER_DATA_MODEL, AccountBaseInfo.class)).willReturn(ACCOUNT_BASE_INFO);

        // when
        AccountBaseInfo result = accountManagementFacade.getAccountBaseInfo(USER_ID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ACCOUNT_BASE_INFO));
    }

    @Test(expected = UserRequestProcessingException.class)
    public void shouldGetAccountBaseInfoThrowUserRequestProcessingExceptionForNullUserID() {

        // when
        accountManagementFacade.getAccountBaseInfo(null);

        // then
        // exception expected
    }

    @Test(expected = UserRequestProcessingException.class)
    public void shouldGetAccountBaseInfoThrowUserRequestProcessingExceptionForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO))
                .willReturn(profileBaseInfoContentRequestAdapter);
        given(profileBaseInfoContentRequestAdapter.getContent(USER_ID)).willReturn(Optional.empty());

        // when
        accountManagementFacade.getAccountBaseInfo(USER_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateAccountBaseInfoReturnWithSuccess() {
        
        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, AccountRequestWrapper<UpdateProfileRequestModel>>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_UPDATE))
                .willReturn(profileUpdateContentRequestAdapter);
        given(profileUpdateContentRequestAdapter.getContent(PROFILE_UPDATE_ACCOUNT_REQUEST_WRAPPER))
                .willReturn(Optional.of(EXTENDED_USER_DATA_MODEL));

        // when
        boolean result = accountManagementFacade.updateAccountBaseInfo(USER_ID, UPDATE_PROFILE_REQUEST_MODEL);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldUpdateAccountBaseInfoReturnWithFailureForNonPersistedUsername() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, AccountRequestWrapper<UpdateProfileRequestModel>>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_UPDATE))
                .willReturn(profileUpdateContentRequestAdapter);
        given(profileUpdateContentRequestAdapter.getContent(PROFILE_UPDATE_ACCOUNT_REQUEST_WRAPPER))
                .willReturn(Optional.of(ExtendedUserDataModel.getExtendedBuilder()
                        .withUsername("different user")
                        .withEmail(EMAIL)
                        .build()));

        // when
        boolean result = accountManagementFacade.updateAccountBaseInfo(USER_ID, UPDATE_PROFILE_REQUEST_MODEL);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldUpdateAccountBaseInfoReturnWithFailureForNonPersistedEmail() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, AccountRequestWrapper<UpdateProfileRequestModel>>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_UPDATE))
                .willReturn(profileUpdateContentRequestAdapter);
        given(profileUpdateContentRequestAdapter.getContent(PROFILE_UPDATE_ACCOUNT_REQUEST_WRAPPER))
                .willReturn(Optional.of(ExtendedUserDataModel.getExtendedBuilder()
                        .withUsername(USERNAME)
                        .withEmail("different email")
                        .build()));

        // when
        boolean result = accountManagementFacade.updateAccountBaseInfo(USER_ID, UPDATE_PROFILE_REQUEST_MODEL);

        // then
        assertThat(result, is(false));
    }

    @Test(expected = UserRequestProcessingException.class)
    public void shouldUpdateAccountBaseInfoThrowUserRequestProcessingExceptionForNullUserID() {

        // when
        accountManagementFacade.updateAccountBaseInfo(null, UPDATE_PROFILE_REQUEST_MODEL);

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateAccountBaseInfoReturnWithFailureForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<ExtendedUserDataModel, AccountRequestWrapper<UpdateProfileRequestModel>>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_UPDATE))
                .willReturn(profileUpdateContentRequestAdapter);
        given(profileUpdateContentRequestAdapter.getContent(PROFILE_UPDATE_ACCOUNT_REQUEST_WRAPPER)).willReturn(Optional.empty());

        // when
        boolean result = accountManagementFacade.updateAccountBaseInfo(USER_ID, UPDATE_PROFILE_REQUEST_MODEL);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldUpdatePasswordReturnWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, AccountRequestWrapper<PasswordChangeRequestModel>>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_PASSWORD_CHANGE))
                .willReturn(passwordUpdateContentRequestAdapter);
        given(passwordUpdateContentRequestAdapter.getContent(PASSWORD_UPDATE_ACCOUNT_REQUEST_WRAPPER)).willReturn(Optional.of(true));

        // when
        boolean result = accountManagementFacade.updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);

        // then
        assertThat(result, is(true));
    }

    @Test(expected = UserRequestProcessingException.class)
    public void shouldUpdatePasswordThrowUserRequestProcessingExceptionForNullUserID() {

        // when
        accountManagementFacade.updatePassword(null, PASSWORD_CHANGE_REQUEST_MODEL);

        // then
        // exception expected
    }

    @Test
    public void shouldUpdatePasswordReturnWithFailureForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, AccountRequestWrapper<PasswordChangeRequestModel>>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_PASSWORD_CHANGE))
                .willReturn(passwordUpdateContentRequestAdapter);
        given(passwordUpdateContentRequestAdapter.getContent(PASSWORD_UPDATE_ACCOUNT_REQUEST_WRAPPER)).willReturn(Optional.empty());

        // when
        boolean result = accountManagementFacade.updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldGetCommentForUserReturnWithSuccess() {

        // given
        given(defaultPaginationAttributes.getLimit()).willReturn(ITEM_LIMIT_ON_PAGE);
        given(defaultPaginationAttributes.getOrderBy()).willReturn(ORDER_BY);
        given(defaultPaginationAttributes.getOrderDirection()).willReturn(ORDER_DIRECTION);
        given(contentRequestAdapterRegistry.<WrapperBodyDataModel<ExtendedCommentListDataModel>, FilteredPaginationContentRequest<Long, OrderBy.Comment>>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENTS_OF_USER))
                .willReturn(userCommentsContentRequestAdapter);
        given(userCommentsContentRequestAdapter.getContent(FILTERED_PAGINATION_CONTENT_REQUEST)).willReturn(Optional.of(WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL));
        given(conversionService.convert(WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL, UserCommentsPageContent.class)).willReturn(USER_COMMENTS_PAGE_CONTENT);

        // when
        UserCommentsPageContent result = accountManagementFacade.getCommentsForUser(USER_ID, PAGE_NUMBER);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(USER_COMMENTS_PAGE_CONTENT));
    }

    @Test(expected = UserRequestProcessingException.class)
    public void shouldGetCommentForUserThrowUserRequestProcessingExceptionForNullUserID() {

        // when
        accountManagementFacade.getCommentsForUser(null, PAGE_NUMBER);

        // then
        // exception expected
    }

    @Test
    public void shouldGetCommentForUserReturnWithEmptyContentObjectForMissingData() {

        // given
        given(defaultPaginationAttributes.getLimit()).willReturn(ITEM_LIMIT_ON_PAGE);
        given(defaultPaginationAttributes.getOrderBy()).willReturn(ORDER_BY);
        given(defaultPaginationAttributes.getOrderDirection()).willReturn(ORDER_DIRECTION);
        given(contentRequestAdapterRegistry.<WrapperBodyDataModel<ExtendedCommentListDataModel>, FilteredPaginationContentRequest<Long, OrderBy.Comment>>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENTS_OF_USER))
                .willReturn(userCommentsContentRequestAdapter);
        given(userCommentsContentRequestAdapter.getContent(FILTERED_PAGINATION_CONTENT_REQUEST)).willReturn(Optional.empty());

        // when
        UserCommentsPageContent result = accountManagementFacade.getCommentsForUser(USER_ID, PAGE_NUMBER);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(UserCommentsPageContent.EMPTY_CONTENT));
    }

    @Test
    public void shouldDeleteAccountReturnWithSuccess() {

        // given
        given(accountDeletionHandler.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST)).willReturn(true);

        // when
        boolean result = accountManagementFacade.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldDeleteAccountReturnWithFailure() {

        // given
        given(accountDeletionHandler.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST)).willReturn(false);

        // when
        boolean result = accountManagementFacade.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldDeleteCommentReturnWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_DELETION))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(COMMENT_ID)).willReturn(Optional.of(true));

        // when
        boolean result = accountManagementFacade.deleteComment(COMMENT_ID);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldDeleteCommentReturnWithFailureForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_DELETION))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(COMMENT_ID)).willReturn(Optional.empty());

        // when
        boolean result = accountManagementFacade.deleteComment(COMMENT_ID);

        // then
        assertThat(result, is(false));
    }
}
