package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.lcfa.core.domain.content.CategorySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Conversion support utility class for optional data parts.
 *
 * @author Peter Smith
 */
@Component
public class FilteringDataConversionSupport {

    private CommentSummaryListTransformer commentSummaryListTransformer;
    private CategorySummaryListConverter categorySummaryListConverter;

    @Autowired
    public FilteringDataConversionSupport(CommentSummaryListTransformer commentSummaryListTransformer, CategorySummaryListConverter categorySummaryListConverter) {
        this.commentSummaryListTransformer = commentSummaryListTransformer;
        this.categorySummaryListConverter = categorySummaryListConverter;
    }

    /**
     * Null-safe mapping for {@link CategoryListDataModel}.
     * Converts {@link CategoryListDataModel} to {@link List} of {@link CategorySummary} objects in case it's not null.
     *
     * @param categoryListDataModel source {@link CategoryListDataModel} object
     * @return mapped {@link List} of {@link CategorySummary} objects or empty list
     */
    public List<CategorySummary> mapCategories(CategoryListDataModel categoryListDataModel) {
        return Optional.ofNullable(categoryListDataModel)
                .map(categorySummaryListConverter::convert)
                .orElse(Collections.emptyList());
    }

    /**
     * Null-safe mapping for {@link CommentListDataModel}.
     * Converts {@link CommentListDataModel} wrapped as {@link WrapperBodyDataModel} to {@link List} of {@link CommentSummary} objects in case it's not null.
     *
     * @param wrappedCommentListDataModel source {@link CommentListDataModel} object wrapped as {@link WrapperBodyDataModel}
     * @param entryDataModel {@link ExtendedEntryDataModel} object required for comment data conversion
     * @return mapped {@link List} of {@link CommentSummary} objects or empty list
     */
    public List<CommentSummary> mapComments(WrapperBodyDataModel<CommentListDataModel> wrappedCommentListDataModel, ExtendedEntryDataModel entryDataModel) {
        return Optional.ofNullable(wrappedCommentListDataModel)
                .map(WrapperBodyDataModel::getBody)
                .map(CommentListDataModel::getComments)
                .map(commentList -> commentSummaryListTransformer.convert(commentList, entryDataModel))
                .orElse(Collections.emptyList());
    }

    /**
     * Null-safe mapping for any other type of {@link BaseBodyDataModel} wrapped as {@link WrapperBodyDataModel}.
     *
     * @param wrappedBodyDataModel source {@link WrapperBodyDataModel} object
     * @param converter converter instance that is able to convert wrapped data to {@link List} of target type
     * @param <S> source object type, must extend {@link BaseBodyDataModel}
     * @param <T> target object type, items of converted {@link List}
     * @return mapped {@link List} of {@code T} objects or empty list
     */
    public <S extends BaseBodyDataModel, T> List<T> mapOptionalWrapped(WrapperBodyDataModel<S> wrappedBodyDataModel, Converter<S, List<T>> converter) {
        return Optional.ofNullable(wrappedBodyDataModel)
                .map(WrapperBodyDataModel::getBody)
                .map(converter::convert)
                .orElse(Collections.emptyList());
    }
}
