package hu.psprog.leaflet.lcfa.core.converter;

import hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link ContactRequestModel} to {@link hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel}.
 *
 * @author Peter Smith
 */
@Component
public class ContactRequestModelConverter implements Converter<ContactRequestModel, hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel> {

    @Override
    public hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel convert(ContactRequestModel source) {

        hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel contactRequestModel = new hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel();
        contactRequestModel.setName(source.getName());
        contactRequestModel.setEmail(source.getEmail());
        contactRequestModel.setMessage(source.getMessage());

        return contactRequestModel;
    }
}
