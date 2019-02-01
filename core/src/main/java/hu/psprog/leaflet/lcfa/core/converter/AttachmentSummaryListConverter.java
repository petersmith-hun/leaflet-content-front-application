package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.AttachmentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.AttachmentType;
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

    @Override
    public List<AttachmentSummary> convert(List<FileDataModel> source) {
        return source.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private AttachmentSummary convert(FileDataModel source) {
        return AttachmentSummary.builder()
                .name(source.getOriginalFilename())
                .description(source.getDescription())
                .link(source.getReference())
                .type(AttachmentType.mapByMime(source.getAcceptedAs()))
                .build();
    }
}
