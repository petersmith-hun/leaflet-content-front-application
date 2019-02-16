package hu.psprog.leaflet.lcfa.web.config;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * {@link TaskDecorator} implementation passing the current request context to the task's thread.
 *
 * @author Peter Smith
 */
public class RequestContextAwareTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        RequestAttributes context = RequestContextHolder.getRequestAttributes();
        HystrixRequestContext hystrixRequestContext = HystrixRequestContext.getContextForCurrentThread();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                HystrixRequestContext.setContextOnCurrentThread(hystrixRequestContext);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
                HystrixRequestContext.setContextOnCurrentThread(null);
            }
        };
    }
}
