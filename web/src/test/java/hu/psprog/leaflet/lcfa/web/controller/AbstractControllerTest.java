package hu.psprog.leaflet.lcfa.web.controller;

import hu.psprog.leaflet.bridge.client.domain.error.ValidationErrorMessageListResponse;
import hu.psprog.leaflet.bridge.client.domain.error.ValidationErrorMessageResponse;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;
import hu.psprog.leaflet.lcfa.core.domain.common.SEOAttributes;
import hu.psprog.leaflet.lcfa.web.factory.ModelAndViewFactory;
import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import hu.psprog.leaflet.lcfa.web.model.ModelField;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private static final String INVALID_FIELD_NAME = "field-1";
    private static final String INVALID_FIELD_VIOLATION = "field restriction violated";
    private static final ValidationErrorMessageListResponse VALIDATION_ERROR_MESSAGE_LIST_RESPONSE = ValidationErrorMessageListResponse.getBuilder()
            .withValidation(Collections.singletonList(ValidationErrorMessageResponse.getExtendedBuilder()
                    .withField(INVALID_FIELD_NAME)
                    .withMessage(INVALID_FIELD_VIOLATION)
                    .build()))
            .build();
    private static final Map<String, String> VALIDATION_RESULTS_MAP = new HashMap<>();
    private static final String SUBMITTED_FORM_VALUES = ModelField.VALIDATED_MODEL.getFieldName();
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed - please see violations";
    private static final String VALIDATION_RESULTS_ATTRIBUTE = "validationResults";
    private static final String FLASH_MESSAGE = ModelField.FLASH.getFieldName();

    static {
        VALIDATION_RESULTS_MAP.put(INVALID_FIELD_NAME, INVALID_FIELD_VIOLATION);
    }

    @Mock
    HttpServletRequest request;

    @Mock
    RedirectAttributes redirectAttributes;

    @Mock
    Response response;

    @Mock
    BindingResult bindingResult;

    @Mock
    private ModelAndViewFactory modelAndViewFactory;

    @Mock
    private ModelAndViewFactory.ModelAndViewWrapper modelAndViewWrapper;

    @Mock
    private ModelAndView modelAndView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(modelAndViewFactory.createForView(anyString())).willReturn(modelAndViewWrapper);
        given(modelAndViewFactory.createRedirectionTo(anyString())).willReturn(modelAndView);
        given(modelAndViewWrapper.withAttribute(anyString(), nullable(Object.class))).willReturn(modelAndViewWrapper);
        given(modelAndViewWrapper.withAttribute(any(ModelField.class), nullable(Object.class))).willReturn(modelAndViewWrapper);
        given(modelAndViewWrapper.build()).willReturn(modelAndView);
        given(response.readEntity(ValidationErrorMessageListResponse.class)).willReturn(VALIDATION_ERROR_MESSAGE_LIST_RESPONSE);
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
