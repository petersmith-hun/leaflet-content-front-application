package hu.psprog.leaflet.lcfa.web.interceptor;

import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageDataField;
import hu.psprog.leaflet.lcfa.core.facade.CommonPageDataFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Interceptor to write {@link CommonPageData} information into the current {@link ModelAndView} instance.
 * This implementation checks whether the current request has {@link ModelAndView} and if the {@link CommonPageData}
 * fields are populated. If any of them is missing, populates it based on the retrieved {@link CommonPageData} object.
 * Already populated fields (for example overridden SEO attributes) are left unchanged.
 *
 * @author Peter Smith
 */
@Component
public class CommonPageDataInterceptor extends HandlerInterceptorAdapter {

    private static final List<String> COMMON_PAGE_DATE_FIELDS = Arrays.stream(CommonPageDataField.values())
            .map(CommonPageDataField::getFieldName)
            .collect(Collectors.toList());

    private CommonPageDataFacade commonPageDataFacade;

    @Autowired
    public CommonPageDataInterceptor(CommonPageDataFacade commonPageDataFacade) {
        this.commonPageDataFacade = commonPageDataFacade;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (Objects.nonNull(modelAndView) && !isCommonPageDataPopulated(modelAndView)) {
            CommonPageData commonPageData = commonPageDataFacade.getCommonPageData();
            Arrays.stream(CommonPageDataField.values()).forEach(field -> {
                if (Objects.isNull(modelAndView.getModel().get(field.getFieldName()))) {
                    modelAndView.getModel().put(field.getFieldName(), field.getMapperFunction().apply(commonPageData));
                }
            });
        }
    }

    private boolean isCommonPageDataPopulated(ModelAndView modelAndView) {
        return modelAndView.getModel().keySet()
                .containsAll(COMMON_PAGE_DATE_FIELDS);
    }

}
