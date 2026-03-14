package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.domain.common.PaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import hu.psprog.leaflet.lcfa.core.facade.AccountManagementFacade;
import hu.psprog.leaflet.lcfa.web.auth.mock.WithMockedJWTUser;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import hu.psprog.leaflet.lcfa.web.model.NavigationItem;
import hu.psprog.leaflet.lcfa.web.ui.support.navigation.AccountNavigationBarSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static hu.psprog.leaflet.lcfa.web.controller.BaseController.PATH_PROFILE_MY_COMMENTS;
import static org.mockito.BDDMockito.given;

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
    private static final List<NavigationItem> NAVIGATION_ITEM_LIST = Collections.singletonList(NavigationItem.build("link", "title"));
    private static final CommentSummary COMMENT_SUMMARY = CommentSummary.builder().content("comment").build();
    private static final PaginationAttributes PAGINATION_ATTRIBUTES = PaginationAttributes.builder().pageNumber(1).build();
    private static final UserCommentsPageContent USER_COMMENTS_PAGE_CONTENT = UserCommentsPageContent.builder()
            .paginationAttributes(PAGINATION_ATTRIBUTES)
            .comments(Collections.singletonList(COMMENT_SUMMARY))
            .build();

    private static final String VIEW_GROUP_ACCOUNT = "account";
    private static final String VIEW_PROFILE = "profile";
    private static final String VIEW_COMMENTS = "comments";

    @Mock
    private AccountManagementFacade accountManagementFacade;

    @Mock
    private AccountNavigationBarSupport accountNavigationBarSupport;

    @Mock
    private PageConfigModel pageConfigModel;

    private AccountController accountController;

    @BeforeEach
    public void setup() {
        super.setup();
        accountController = new AccountController(modelAndViewFactory, accountManagementFacade, accountNavigationBarSupport, pageConfigModel);
    }

    @Test
    public void shouldRenderProfileMainScreen() {

        // given
        given(accountNavigationBarSupport.profile()).willReturn(NAVIGATION_ITEM_LIST);

        // when
        accountController.renderProfileMainScreen();

        // then
        verifyViewCreated(VIEW_PROFILE);
        verifyFieldSet(ModelField.NAVIGATION, NAVIGATION_ITEM_LIST);
    }

    @Test
    public void shouldRedirectToProfileManagement() {

        // given
        var profileManagementEndpoint = "http://localhost:9000/profile";

        given(pageConfigModel.getProfileManagementEndpoint()).willReturn(profileManagementEndpoint);

        // when
        accountController.redirectToProfileManagement();

        // then
        verifyRedirectionCreated(profileManagementEndpoint);
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
        verifyFieldSet(ModelField.COMMENTS, USER_COMMENTS_PAGE_CONTENT.comments());
        verifyFieldSet(ModelField.PAGINATION, USER_COMMENTS_PAGE_CONTENT.paginationAttributes());
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
