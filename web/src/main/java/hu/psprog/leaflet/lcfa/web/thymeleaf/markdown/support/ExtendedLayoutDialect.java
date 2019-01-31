package hu.psprog.leaflet.lcfa.web.thymeleaf.markdown.support;

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

    public ExtendedLayoutDialect(Parser parser, HtmlRenderer htmlRenderer) {
        this.parser = parser;
        this.htmlRenderer = htmlRenderer;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {

        Set<IProcessor> processors = super.getProcessors(dialectPrefix);
        processors.add(new MarkdownAttributeTagProcessor(dialectPrefix, parser, htmlRenderer));

        return processors;
    }
}
