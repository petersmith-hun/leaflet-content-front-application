package hu.psprog.leaflet.lcfa.web.interceptor;

import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;
import hu.psprog.leaflet.lcfa.core.facade.CommonPageDataFacade;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Interceptor to write {@link CommonPageData} information into the current {@link ModelAndView} instance.
 * This implementation checks whether the current request has {@link ModelAndView} and if the {@link CommonPageData}
 * fields are populated. If any of them is missing, populates it based on the retrieved {@link CommonPageData} object.
 * Already populated fields (for example overridden SEO attributes) are left unchanged.
 *
 * Also adds contents of {@link PageConfigModel}.
 *
 * @author Peter Smith
 */
@Component
public class CommonPageDataInterceptor implements HandlerInterceptor {

    private static final String PAGE_CONFIG_ATTRIBUTE = "pageConfig";
    private static final List<String> COMMON_PAGE_DATE_FIELDS = Arrays.stream(CommonPageDataField.values())
            .map(CommonPageDataField::getFieldName)
            .toList();

    private final CommonPageDataFacade commonPageDataFacade;
    private final PageConfigModel pageConfigModel;

    @Autowired
    public CommonPageDataInterceptor(CommonPageDataFacade commonPageDataFacade, PageConfigModel pageConfigModel) {
        this.commonPageDataFacade = commonPageDataFacade;
        this.pageConfigModel = pageConfigModel;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        if (Objects.nonNull(modelAndView) && isRequestDispatcher(request)) {
            if (!isCommonPageDataPopulated(modelAndView)) {
                CommonPageData commonPageData = commonPageDataFacade.getCommonPageData();
                Arrays.stream(CommonPageDataField.values()).forEach(field -> {
                    if (Objects.isNull(modelAndView.getModel().get(field.getFieldName()))) {
                        modelAndView.getModel().put(field.getFieldName(), field.getMapperFunction().apply(commonPageData));
                    }
                });
                modelAndView.addObject(PAGE_CONFIG_ATTRIBUTE, pageConfigModel);
            }

            modelAndView.addObject("request", request);
        }
    }

    private boolean isRequestDispatcher(HttpServletRequest request) {
        return request.getDispatcherType() == DispatcherType.REQUEST;
    }

    private boolean isCommonPageDataPopulated(ModelAndView modelAndView) {
        return modelAndView.getModel().keySet()
                .containsAll(COMMON_PAGE_DATE_FIELDS);
    }

}
