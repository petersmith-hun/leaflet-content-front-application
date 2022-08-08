package hu.psprog.leaflet.lcfa.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import hu.psprog.leaflet.lcfa.core.utility.ResourcePathResolver;
import hu.psprog.leaflet.lcfa.web.interceptor.CommonPageDataInterceptor;
import hu.psprog.leaflet.lcfa.web.interceptor.ModelAndViewDebuggerInterceptor;
import hu.psprog.leaflet.lcfa.web.thymeleaf.support.markdown.ExtendedLayoutDialect;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
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

    private final WebAppResources webAppResources;
    private final CommonPageDataInterceptor commonPageDataInterceptor;

    @Autowired
    private Optional<ModelAndViewDebuggerInterceptor> modelAndViewDebuggerInterceptor;

    @Autowired
    public WebMVCConfiguration(WebAppResources webAppResources, CommonPageDataInterceptor commonPageDataInterceptor) {
        this.webAppResources = webAppResources;
        this.commonPageDataInterceptor = commonPageDataInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        modelAndViewDebuggerInterceptor.ifPresent(registry::addInterceptor);
        registry.addInterceptor(commonPageDataInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        webAppResources.getResources()
                .forEach(resource -> registry
                        .addResourceHandler(resource.getHandler())
                        .addResourceLocations(resource.getLocation()));
    }

    @Bean
    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter(Jackson2ObjectMapperBuilder builder) {

        ObjectMapper objectMapper = builder.createXmlMapper(true).build();
        ((XmlMapper) objectMapper).enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);

        return new MappingJackson2XmlHttpMessageConverter(objectMapper);
    }

    @Bean
    public Parser commonmarkParser() {
        return Parser.builder().build();
    }

    @Bean
    public HtmlRenderer commonmarkHtmlRenderer() {
        return HtmlRenderer.builder().build();
    }

    @Bean
    @Primary
    public LayoutDialect layoutDialect(Parser commonmarkParser, HtmlRenderer commonmarkHtmlRenderer, ResourcePathResolver resourcePathResolver) {
        return new ExtendedLayoutDialect(commonmarkParser, commonmarkHtmlRenderer, resourcePathResolver);
    }
}
