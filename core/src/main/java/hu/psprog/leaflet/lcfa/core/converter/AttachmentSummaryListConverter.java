package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.AttachmentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.AttachmentType;
import hu.psprog.leaflet.lcfa.core.utility.ResourcePathResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts {@link List} of {@link FileDataModel} objects to {@link List} of {@link AttachmentSummary} objects.
 *
 * @author Peter Smith
 */
@Component
public class AttachmentSummaryListConverter implements Converter<List<FileDataModel>, List<AttachmentSummary>> {

    private final ResourcePathResolver resourcePathResolver;

    @Autowired
    public AttachmentSummaryListConverter(ResourcePathResolver resourcePathResolver) {
        this.resourcePathResolver = resourcePathResolver;
    }

    @Override
    public List<AttachmentSummary> convert(List<FileDataModel> source) {
        return source.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private AttachmentSummary convert(FileDataModel source) {
        return AttachmentSummary.builder()
                .name(source.originalFilename())
                .description(source.description())
                .link(resourcePathResolver.resolve(source.reference()))
                .type(AttachmentType.mapByMime(source.acceptedAs()))
                .build();
    }
}
