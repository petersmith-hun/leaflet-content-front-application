package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.AuthenticationFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AuthenticationFacade}.
 *
 * @author Peter Smith
 */
@Service
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Autowired
    public AuthenticationFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
    }

    @Override
    public void signUp(SignUpRequestModel signUpRequestModel) {
        contentRequestAdapterRegistry.<Boolean, SignUpRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.SIGN_UP)
                .getContent(signUpRequestModel)
                .orElseThrow(() -> new UserRequestProcessingException("Sign-up request could not be processed."));
    }
}
