package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Converts {@link SignUpRequestModel} to {@link UserInitializeRequestModel}.
 *
 * @author Peter Smith
 */
@Component
public class SignUpRequestConverter implements Converter<SignUpRequestModel, UserInitializeRequestModel> {

    private static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("hu_HU");

    @Override
    public UserInitializeRequestModel convert(SignUpRequestModel source) {

        UserInitializeRequestModel userInitializeRequestModel = new UserInitializeRequestModel();
        userInitializeRequestModel.setDefaultLocale(DEFAULT_LOCALE);
        userInitializeRequestModel.setUsername(source.getUsername());
        userInitializeRequestModel.setEmail(source.getEmail());
        userInitializeRequestModel.setPassword(source.getPassword());
        userInitializeRequestModel.setPasswordConfirmation(source.getPasswordConfirmation());

        return userInitializeRequestModel;
    }
}
