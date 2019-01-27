package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.TagSummary;
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

    @Override
    public List<TagSummary> convert(TagListDataModel source) {
        return source.getTags().stream()
                .map(this::createTagSummary)
                .collect(Collectors.toList());
    }

    private TagSummary createTagSummary(TagDataModel tagDataModel) {
        return new TagSummary(tagDataModel.getId(), tagDataModel.getName());
    }
}
