package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.SignUpResult;
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
    public SignUpResult signUp(SignUpRequestModel signUpRequestModel) {
        return contentRequestAdapterRegistry.<SignUpResult, SignUpRequestModel>getContentRequestAdapter(ContentRequestAdapterIdentifier.SIGN_UP)
                .getContent(signUpRequestModel)
                .orElse(SignUpResult.FAILURE);
    }
}
