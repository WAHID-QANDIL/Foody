package org.wahid.foody.data.remote.user_auth;

import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;
import org.wahid.foody.presentation.auth.AuthCredentials;
import org.wahid.foody.presentation.auth.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository<AuthCredentials> {

    private final AuthenticationService<AuthCredentials> authenticationService;

    @SuppressWarnings("unchecked")
    public AuthRepositoryImpl(AuthenticationServiceType authenticationServiceType) {
        AuthenticationServiceFactory serviceFactory = new AuthenticationServiceFactory();
        // The factory returns the correct service for the type, we cast to the generic interface
        this.authenticationService = (AuthenticationService<AuthCredentials>)
                serviceFactory.getAuthenticationInstance(authenticationServiceType);
    }

    @Override
    public void login(AuthCredentials credentials, OnAuthenticatedCallBack callBack) {
        authenticationService.login(credentials, callBack);
    }

    @Override
    public void register(AuthCredentials credentials, OnAuthenticatedCallBack callBack) {
        authenticationService.register(credentials, callBack);
    }
}