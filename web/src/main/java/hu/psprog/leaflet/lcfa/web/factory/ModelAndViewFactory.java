package hu.psprog.leaflet.lcfa.web.factory;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * Factory for creating {@link ModelAndView} objects.
 *
 * @author Peter Smith
 */
@Component
public class ModelAndViewFactory {

    private static final String PATH_SUFFIX_REDIRECT = "redirect:";

    /**
     * Creates a new {@link ModelAndViewWrapper} instance, which is a fluid-like {@link ModelAndView} object builder.
     *
     * @param viewName name of the view
     * @return initialized ModelAndViewWrapper instance
     */
    public ModelAndViewWrapper createForView(String viewName) {
        return new ModelAndViewWrapper(viewName);
    }

    /**
     * Creates a redirection view for the given path.
     *
     * @param path path to redirect to
     * @return initialized ModelAndView instance
     */
    public ModelAndView createRedirectionTo(String path) {
        return new ModelAndView(PATH_SUFFIX_REDIRECT + path);
    }

    /**
     * Builder class for creating {@link ModelAndView} objects.
     */
    public static class ModelAndViewWrapper {

        private ModelAndView modelAndView;

        private ModelAndViewWrapper(String viewName) {
            this.modelAndView = new ModelAndView(viewName);
        }

        /**
         * Adds an attribute to the {@link ModelAndView}.
         *
         * @param key attribute key
         * @param value attribute value
         * @return current ModelAndViewWrapper instance
         */
        public ModelAndViewWrapper withAttribute(String key, Object value) {
            modelAndView.addObject(key, value);
            return this;
        }

        /**
         * Returns built {@link ModelAndView} instance.
         *
         * @return built {@link ModelAndView} instance
         */
        public ModelAndView build() {
            return modelAndView;
        }
    }
}
