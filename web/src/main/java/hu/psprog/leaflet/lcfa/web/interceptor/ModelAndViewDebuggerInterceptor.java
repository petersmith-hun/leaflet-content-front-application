package hu.psprog.leaflet.lcfa.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Debugger interceptor to log parameters added to the current model.
 *
 * @author Peter Smith
 */
@Component
@Profile("debug")
public class ModelAndViewDebuggerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelAndViewDebuggerInterceptor.class);
    private static final String MODEL_ENTRY_PATTERN = "%60s : %s";
    private static final String DEBUG_MESSAGE_PREFIX = "Model contents" + System.lineSeparator();
    private static final String DEBUG_MESSAGE_SUFFIX = System.lineSeparator();
    private static final String NO_MAV_INSTANCE_PATTERN = "No ModelAndView instance is available to debug on path [%s]";

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        LOGGER.info(createModelContentDebugString(modelAndView, request));
    }

    private String createModelContentDebugString(ModelAndView modelAndView, HttpServletRequest request) {

        return Optional.ofNullable(modelAndView)
                .map(nonNullModelAndView -> nonNullModelAndView.getModel().entrySet().stream()
                        .map(entry -> String.format(MODEL_ENTRY_PATTERN, entry.getKey(), entry.getValue()))
                        .collect(Collectors.joining(System.lineSeparator(), DEBUG_MESSAGE_PREFIX, DEBUG_MESSAGE_SUFFIX)))
                .orElse(String.format(NO_MAV_INSTANCE_PATTERN, request.getServletPath()));
    }
}
