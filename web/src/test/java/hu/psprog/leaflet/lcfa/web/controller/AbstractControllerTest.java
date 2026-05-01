package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;
import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Base test for controllers.
 *
 * @author Peter Smith
 */
public abstract class AbstractControllerTest {

    private static final String VIEW_NAME_FORMAT = "view/%s/%s";
    private static final String FLASH_MESSAGE = ModelField.FLASH.getFieldName();

    @Mock
    RedirectAttributes redirectAttributes;

    @Mock
    BindingResult bindingResult;

    @Mock(strictness = Mock.Strictness.LENIENT)
    ModelAndViewFactory modelAndViewFactory;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private ModelAndViewFactory.ModelAndViewWrapper modelAndViewWrapper;

    @Mock
    private ModelAndView modelAndView;

    @BeforeEach
    public void setup() {
        given(modelAndViewFactory.createForView(anyString())).willReturn(modelAndViewWrapper);
        given(modelAndViewFactory.createRedirectionTo(anyString())).willReturn(modelAndView);
        given(modelAndViewWrapper.withAttribute(anyString(), nullable(Object.class))).willReturn(modelAndViewWrapper);
        given(modelAndViewWrapper.withAttribute(any(ModelField.class), nullable(Object.class))).willReturn(modelAndViewWrapper);
        given(modelAndViewWrapper.build()).willReturn(modelAndView);
    }

    void verifyViewCreated(String name) {
        verify(modelAndViewFactory).createForView(String.format(VIEW_NAME_FORMAT, controllerViewGroup(), name));
    }

    void verifyRedirectionCreated(String to) {
        verify(modelAndViewFactory).createRedirectionTo(to);
    }

    void verifyFieldSet(ModelField modelField, Object content) {
        verify(modelAndViewWrapper).withAttribute(modelField, content);
    }

    void verifySeoOverride(SEOAttributes seoAttributes) {
        verify(modelAndViewWrapper).withAttribute(CommonPageDataField.SEO_ATTRIBUTES.getFieldName(), seoAttributes);
    }

    void verifyFlashMessageSet(FlashMessageKey flashMessageKey) {
        verify(redirectAttributes).addFlashAttribute(FLASH_MESSAGE, flashMessageKey.getMessageKey());
    }

    abstract String controllerViewGroup();
}
