package hu.psprog.leaflet.lcfa.web.thymeleaf.markdown.support;

import hu.psprog.leaflet.lcfa.core.utility.ResourcePathResolver;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

/**
 * {@link LayoutDialect} extension to include {@link MarkdownAttributeTagProcessor} as an additional Thymeleaf processor.
 *
 * @author Peter Smith
 */
public class ExtendedLayoutDialect extends LayoutDialect {

    private Parser parser;
    private HtmlRenderer htmlRenderer;
    private ResourcePathResolver resourcePathResolver;

    public ExtendedLayoutDialect(Parser parser, HtmlRenderer htmlRenderer, ResourcePathResolver resourcePathResolver) {
        this.parser = parser;
        this.htmlRenderer = htmlRenderer;
        this.resourcePathResolver = resourcePathResolver;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {

        Set<IProcessor> processors = super.getProcessors(dialectPrefix);
        processors.add(new MarkdownAttributeTagProcessor(dialectPrefix, parser, htmlRenderer, resourcePathResolver));

        return processors;
    }
}
