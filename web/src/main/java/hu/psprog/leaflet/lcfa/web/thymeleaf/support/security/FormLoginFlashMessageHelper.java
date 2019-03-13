package hu.psprog.leaflet.lcfa.web.thymeleaf.support.security;

import hu.psprog.leaflet.lcfa.web.model.FlashMessageKey;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility to map sign-in/out result to flash message.
 *
 * @author Peter Smith
 */
@Component
public class FormLoginFlashMessageHelper {

    private static final Map<String, String> MESSAGE_KEY_MAP = new HashMap<>();
    private static final String SIGN_IN_RESULT_KEY = "auth";

    static {
        MESSAGE_KEY_MAP.put("success", FlashMessageKey.SUCCESSFUL_SIGN_IN.getMessageKey());
        MESSAGE_KEY_MAP.put("failure", FlashMessageKey.FAILED_SIGN_IN.getMessageKey());
        MESSAGE_KEY_MAP.put("signout", FlashMessageKey.SUCCESSFUL_SIGN_OUT.getMessageKey());
    }

    /**
     * Maps the current sign-in/out result (extracted from {@link HttpServletRequest}) to the relevant flash message key.
     * Uses the 'auth' query parameter for decision.
     * If a flash message key is already set, leaves intact.
     *
     * @param request {@link HttpServletRequest} object to extract sign-in/out result from
     * @param currentFlashMessageKey currently set flash message key (can be {@code null})
     * @return resolved flash message key, or {@code null} if none set
     */
    public String getMessageKey(HttpServletRequest request, String currentFlashMessageKey) {

        String messageKey = currentFlashMessageKey;
        if (Objects.isNull(messageKey)) {
            String authQueryParameterValue = request.getParameter(SIGN_IN_RESULT_KEY);
            messageKey = MESSAGE_KEY_MAP.getOrDefault(authQueryParameterValue, null);
        }

        return messageKey;
    }
}
