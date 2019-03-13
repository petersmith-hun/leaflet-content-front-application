package hu.psprog.leaflet.lcfa.web.thymeleaf.support.markdown;

import hu.psprog.leaflet.lcfa.core.utility.ResourcePathResolver;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.EngineEventUtils;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Objects;

/**
 * Thymeleaf {@link AbstractAttributeTagProcessor} implementation for rendering {@code th:markdown} attributes.
 * Implementation uses Atlassian Commonmark markdown rendering engine.
 *
 * @author Peter Smith
 */
public class MarkdownAttributeTagProcessor extends AbstractAttributeTagProcessor {

    private static final String PROCESSOR_NAME = "th:markdown";
    private static final int PROCESSOR_PRECEDENCE = 0;

    private final Parser parser;
    private final HtmlRenderer htmlRenderer;
    private final ResourcePathResolver resourcePathResolver;

    protected MarkdownAttributeTagProcessor(String dialectPrefix, Parser parser, HtmlRenderer htmlRenderer, ResourcePathResolver resourcePathResolver) {
        super(TemplateMode.HTML, dialectPrefix, null, true, PROCESSOR_NAME, false, PROCESSOR_PRECEDENCE, true);
        this.parser = parser;
        this.htmlRenderer = htmlRenderer;
        this.resourcePathResolver = resourcePathResolver;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        if (Objects.nonNull(attributeValue)) {
            Object expressionResult = evaluateExpressions(context, tag, attributeName, attributeValue);
            if (Objects.nonNull(expressionResult)) {
                String resourceProcessed = resourcePathResolver.resolveInMarkdownSource(expressionResult.toString());
                Node node = parser.parse(resourceProcessed);
                structureHandler.setBody(htmlRenderer.render(node), true);
            }
        }
    }

    private Object evaluateExpressions(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue) {
        return EngineEventUtils
                .computeAttributeExpression(context, tag, attributeName, attributeValue)
                .execute(context);
    }
}
