package org.wahid.foody.data.user_auth;

import org.wahid.foody.data.user_auth.core.AuthenticationService;
import org.wahid.foody.data.user_auth.core.AuthenticationServiceFactory;
import org.wahid.foody.data.user_auth.core.AuthenticationServiceType;
import org.wahid.foody.data.user_auth.core.UserCredentials;
import org.wahid.foody.presentation.auth.AuthCredentials;
import org.wahid.foody.domain.repository.AuthRepository;

import io.reactivex.rxjava3.core.Single;

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
    public Single<UserCredentials<?>> login(AuthCredentials credentials) {
        return authenticationService.login(credentials);
    }

    @Override
    public Single<UserCredentials<?>> register(AuthCredentials credentials) {
        return authenticationService.register(credentials);
    }
}