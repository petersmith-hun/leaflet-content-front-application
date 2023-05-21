package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.lcfa.core.domain.account.AccountBaseInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link ExtendedUserDataModel} to {@link AccountBaseInfo} object.
 *
 * @author Peter Smith
 */
@Component
public class AccountBaseInfoConverter implements Converter<ExtendedUserDataModel, AccountBaseInfo> {

    @Override
    public AccountBaseInfo convert(ExtendedUserDataModel source) {
        return AccountBaseInfo.builder()
                .email(source.email())
                .username(source.username())
                .locale(source.locale())
                .build();
    }
}
