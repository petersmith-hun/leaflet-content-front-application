package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.account.AccountBaseInfo;
import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountDeletionRequest;
import hu.psprog.leaflet.lcfa.core.facade.AccountManagementFacade;
import hu.psprog.leaflet.lcfa.web.auth.mock.WithMockedJWTUser;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.AccountNavigationBarSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_HOME;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_CHANGE_PASSWORD;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_DELETE_ACCOUNT;
import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_MY_COMMENTS;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit tests for {@link AccountController}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(MockitoExtension.class),
        @ExtendWith(SpringExtension.class)
})
@WithMockedJWTUser
public class AccountControllerTest extends AbstractControllerTest {

    private static final long USER_ID = 1L;
    private static final long COMMENT_ID = 8L;
    private static final AccountBaseInfo ACCOUNT_BASE_INFO = AccountBaseInfo.builder().username("username").build();
    private static final List<NavigationItem> NAVIGATION_ITEM_LIST = Collections.singletonList(NavigationItem.build("link", "title"));
    private static final CommentSummary COMMENT_SUMMARY = CommentSummary.builder().content("comment").build();
    private static final PaginationAttributes PAGINATION_ATTRIBUTES = PaginationAttributes.builder().pageNumber(1).build();
    private static final UserCommentsPageContent USER_COMMENTS_PAGE_CONTENT = UserCommentsPageContent.builder()
            .paginationAttributes(PAGINATION_ATTRIBUTES)
            .comments(Collections.singletonList(COMMENT_SUMMARY))
            .build();

    private static final UpdateProfileRequestModel UPDATE_PROFILE_REQUEST_MODEL = new UpdateProfileRequestModel();
    private static final PasswordChangeRequestModel PASSWORD_CHANGE_REQUEST_MODEL = new PasswordChangeRequestModel();
    private static final AccountDeletionRequest ACCOUNT_DELETION_REQUEST = new AccountDeletionRequest();

    private static final String VIEW_GROUP_ACCOUNT = "account";
    private static final String VIEW_PROFILE = "profile";
    private static final String VIEW_PASSWORD_CHANGE = "pw_change";
    private static final String VIEW_COMMENTS = "comments";
    private static final String VIEW_DELETE = "delete";

    static {
        UPDATE_PROFILE_REQUEST_MODEL.setUsername("new username");
        PASSWORD_CHANGE_REQUEST_MODEL.setCurrentPassword("current-password");
        ACCOUNT_DELETION_REQUEST.setPassword("password");
    }

    @Mock
    private AccountManagementFacade accountManagementFacade;

    @Mock
    private AccountNavigationBarSupport accountNavigationBarSupport;

    @InjectMocks
    private AccountController accountController;

    @Test
    public void shouldRenderProfileForm() {

        // given
        given(accountManagementFacade.getAccountBaseInfo(USER_ID)).willReturn(ACCOUNT_BASE_INFO);
        given(accountNavigationBarSupport.profile()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        accountController.renderProfileForm(UPDATE_PROFILE_REQUEST_MODEL);

        // then
        verify(accountManagementFacade).getAccountBaseInfo(USER_ID);
        verifyViewCreated(VIEW_PROFILE);
        verifyFieldSet(ModelField.ACCOUNT, ACCOUNT_BASE_INFO);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    public void shouldProcessProfileUpdateRequestWithSuccess() {

        // given
        given(accountManagementFacade.updateAccountBaseInfo(USER_ID, UPDATE_PROFILE_REQUEST_MODEL)).willReturn(true);

        // when
        accountController.processProfileUpdateRequest(UPDATE_PROFILE_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyFlashMessageSet(FlashMessageKey.SUCCESSFUL_PROFILE_UPDATE);
        verifyRedirectionCreated(PATH_PROFILE);
    }

    @Test
    public void shouldProcessProfileUpdateRequestWithFailure() {

        // given
        given(accountManagementFacade.updateAccountBaseInfo(USER_ID, UPDATE_PROFILE_REQUEST_MODEL)).willReturn(false);

        // when
        accountController.processProfileUpdateRequest(UPDATE_PROFILE_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyFlashMessageSet(FlashMessageKey.FAILED_PROFILE_UPDATE);
        verifyRedirectionCreated(PATH_PROFILE);
    }

    @Test
    public void shouldProcessProfileUpdateRequestWithValidatorError() {

        // given
        given(bindingResult.hasErrors()).willReturn(true);
        given(accountManagementFacade.getAccountBaseInfo(USER_ID)).willReturn(ACCOUNT_BASE_INFO);
        given(accountNavigationBarSupport.profile()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        accountController.processProfileUpdateRequest(UPDATE_PROFILE_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verify(accountManagementFacade).getAccountBaseInfo(USER_ID);
        verifyNoMoreInteractions(accountManagementFacade);
        verifyViewCreated(VIEW_PROFILE);
        verifyFieldSet(ModelField.ACCOUNT, ACCOUNT_BASE_INFO);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    public void shouldRenderPasswordChangeForm() {

        // given
        given(accountNavigationBarSupport.passwordChange()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        accountController.renderPasswordChangeForm(PASSWORD_CHANGE_REQUEST_MODEL);

        // then
        verifyViewCreated(VIEW_PASSWORD_CHANGE);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    public void shouldProcessPasswordChangeRequestWithSuccess() {

        // given
        given(accountManagementFacade.updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL)).willReturn(true);

        // when
        accountController.processPasswordChangeRequest(PASSWORD_CHANGE_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verify(accountManagementFacade).updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);
        verifyFlashMessageSet(FlashMessageKey.SUCCESSFUL_PROFILE_UPDATE);
        verifyRedirectionCreated(PATH_PROFILE_CHANGE_PASSWORD);
    }

    @Test
    public void shouldProcessPasswordChangeRequestWithFailure() {

        // given
        given(accountManagementFacade.updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL)).willReturn(false);

        // when
        accountController.processPasswordChangeRequest(PASSWORD_CHANGE_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verify(accountManagementFacade).updatePassword(USER_ID, PASSWORD_CHANGE_REQUEST_MODEL);
        verifyFlashMessageSet(FlashMessageKey.FAILED_PROFILE_UPDATE);
        verifyRedirectionCreated(PATH_PROFILE_CHANGE_PASSWORD);
    }

    @Test
    public void shouldProcessPasswordChangeRequestWithValidationError() {

        // given
        given(bindingResult.hasErrors()).willReturn(true);
        given(accountNavigationBarSupport.passwordChange()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        accountController.processPasswordChangeRequest(PASSWORD_CHANGE_REQUEST_MODEL, bindingResult, redirectAttributes);

        // then
        verifyViewCreated(VIEW_PASSWORD_CHANGE);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
        verifyNoInteractions(accountManagementFacade);
    }

    @ParameterizedTest
    @MethodSource("pageParameterDataProvider")
    public void shouldRenderComments(Integer receivedPageNumber, int expectedPageNumber) {

        // given
        given(accountManagementFacade.getCommentsForUser(USER_ID, expectedPageNumber)).willReturn(USER_COMMENTS_PAGE_CONTENT);
        given(accountNavigationBarSupport.myComments()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        accountController.renderComments(Optional.ofNullable(receivedPageNumber));

        // then
        verifyViewCreated(VIEW_COMMENTS);
        verifyFieldSet(ModelField.COMMENTS, USER_COMMENTS_PAGE_CONTENT.getComments());
        verifyFieldSet(ModelField.PAGINATION, USER_COMMENTS_PAGE_CONTENT.getPaginationAttributes());
        verifyFieldSet(ModelField.LINK_TEMPLATE, "/profile/my-comments/{page}");
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    public void shouldDeleteCommentWithSuccess() {

        // given
        given(accountManagementFacade.deleteComment(COMMENT_ID)).willReturn(true);

        // when
        accountController.deleteComment(COMMENT_ID, redirectAttributes);

        // then
        verifyFlashMessageSet(FlashMessageKey.SUCCESSFUL_COMMENT_DELETION);
        verifyRedirectionCreated(PATH_PROFILE_MY_COMMENTS);
    }

    @Test
    public void shouldDeleteCommentWithFailure() {

        // given
        given(accountManagementFacade.deleteComment(COMMENT_ID)).willReturn(false);

        // when
        accountController.deleteComment(COMMENT_ID, redirectAttributes);

        // then
        verifyFlashMessageSet(FlashMessageKey.FAILED_COMMENT_DELETION);
        verifyRedirectionCreated(PATH_PROFILE_MY_COMMENTS);
    }

    @Test
    public void shouldRenderAccountDeletionForm() {

        // given
        given(accountNavigationBarSupport.accountDeletion()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        accountController.renderAccountDeletionForm();

        // then
        verifyViewCreated(VIEW_DELETE);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    public void shouldProcessAccountDeletionRequestWithSuccess() {

        // given
        given(accountManagementFacade.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST)).willReturn(true);

        // when
        accountController.processAccountDeletionRequest(ACCOUNT_DELETION_REQUEST, redirectAttributes);

        // then
        verifyRedirectionCreated(PATH_HOME);
        verifyFlashMessageSet(FlashMessageKey.SUCCESSFUL_ACCOUNT_DELETION);
    }

    @Test
    public void shouldProcessAccountDeletionRequestWithFailure() {

        // given
        given(accountManagementFacade.deleteAccount(USER_ID, ACCOUNT_DELETION_REQUEST)).willReturn(false);

        // when
        accountController.processAccountDeletionRequest(ACCOUNT_DELETION_REQUEST, redirectAttributes);

        // then
        verifyRedirectionCreated(PATH_PROFILE_DELETE_ACCOUNT);
        verifyFlashMessageSet(FlashMessageKey.FAILED_ACCOUNT_DELETION);
    }

    @Override
    String controllerViewGroup() {
        return VIEW_GROUP_ACCOUNT;
    }

    private static Stream<Arguments> pageParameterDataProvider() {

        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of(1, 1),
                Arguments.of(5, 5)
        );
    }
}
