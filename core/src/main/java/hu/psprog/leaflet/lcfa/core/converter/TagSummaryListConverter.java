package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts {@link TagListDataModel} to {@link List} of {@link TagSummary} objects.
 *
 * @author Peter Smith
 */
@Component
public class TagSummaryListConverter implements Converter<TagListDataModel, List<TagSummary>> {

    private LinkAliasGenerator linkAliasGenerator;

    @Autowired
    public TagSummaryListConverter(LinkAliasGenerator linkAliasGenerator) {
        this.linkAliasGenerator = linkAliasGenerator;
    }

    @Override
    public List<TagSummary> convert(TagListDataModel source) {
        return source.getTags().stream()
                .map(this::createTagSummary)
                .collect(Collectors.toList());
    }

    private TagSummary createTagSummary(TagDataModel tagDataModel) {
        return TagSummary.builder()
                .id(tagDataModel.getId())
                .name(tagDataModel.getName())
                .alias(linkAliasGenerator.generateAlias(tagDataModel.getName()))
                .build();
    }
}
