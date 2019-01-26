package hu.psprog.leaflet.lcfa.web.config;

import hu.psprog.leaflet.lcfa.web.interceptor.ModelAndViewDebuggerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

/**
 * Spring web MVC configuration.
 *
 * @author Peter Smith
 */
@Configuration
public class WebMVCConfiguration implements WebMvcConfigurer {

    private WebAppResources webAppResources;
    private Optional<ModelAndViewDebuggerInterceptor> modelAndViewDebuggerInterceptor;

    @Autowired
    public WebMVCConfiguration(WebAppResources webAppResources, Optional<ModelAndViewDebuggerInterceptor> modelAndViewDebuggerInterceptor) {
        this.webAppResources = webAppResources;
        this.modelAndViewDebuggerInterceptor = modelAndViewDebuggerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        modelAndViewDebuggerInterceptor.ifPresent(registry::addInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        webAppResources.getResources()
                .forEach(resource -> registry
                        .addResourceHandler(resource.getHandler())
                        .addResourceLocations(resource.getLocation()));
    }
}
