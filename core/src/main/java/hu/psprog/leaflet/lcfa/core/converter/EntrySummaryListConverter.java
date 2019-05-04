package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.AuthorSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.EntrySummary;
import hu.psprog.leaflet.lcfa.core.formatter.DateFormatterUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts {@link EntryListDataModel} to {@link List} of {@link EntrySummary} objects.
 *
 * @author Peter Smith
 */
@Component
public class EntrySummaryListConverter implements Converter<EntryListDataModel, List<EntrySummary>> {

    private DateFormatterUtility dateFormatterUtility;

    @Autowired
    public EntrySummaryListConverter(DateFormatterUtility dateFormatterUtility) {
        this.dateFormatterUtility = dateFormatterUtility;
    }

    @Override
    public List<EntrySummary> convert(EntryListDataModel source) {
        return source.getEntries().stream()
                .map(this::createEntrySummary)
                .collect(Collectors.toList());
    }

    private EntrySummary createEntrySummary(EntryDataModel entryDataModel) {
        return EntrySummary.builder()
                .link(entryDataModel.getLink())
                .title(entryDataModel.getTitle())
                .author(createAuthorSummary(entryDataModel))
                .creationDate(dateFormatterUtility.formatGeneral(extractCreationDate(entryDataModel)))
                .prologue(entryDataModel.getPrologue())
                .build();
    }

    private AuthorSummary createAuthorSummary(EntryDataModel entryDataModel) {
        return new AuthorSummary(entryDataModel.getUser().getUsername());
    }

    private ZonedDateTime extractCreationDate(EntryDataModel entryDataModel) {
        return Optional.ofNullable(entryDataModel.getPublished())
                .orElse(entryDataModel.getCreated());
    }
}
