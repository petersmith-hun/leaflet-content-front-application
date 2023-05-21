package hu.psprog.leaflet.lcfa.web.config;

import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.filter.RequestContextFilter;

import java.util.Collections;

/**
 * Main application context configuration.
 *
 * @author Peter Smith
 */
@Configuration
public class ApplicationContextConfig {

    @Bean
    public AsyncTaskExecutor contentAdapterAsyncTaskExecutor() {

        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setTaskDecorator(new RequestContextAwareTaskDecorator());

        return simpleAsyncTaskExecutor;
    }

    @Bean
    public RequestContextFilter requestContextFilter() {
        OrderedRequestContextFilter orderedRequestContextFilter = new OrderedRequestContextFilter();
        orderedRequestContextFilter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return orderedRequestContextFilter;
    }

    @Bean
    @ConditionalOnMissingBean(RequestAuthentication.class)
    public RequestAuthentication defaultRequestAuthentication() {
        return Collections::emptyMap;
    }
}
