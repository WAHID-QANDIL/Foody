package org.wahid.foody.data.user_auth.core;

import io.reactivex.rxjava3.core.Single;

// Base for all authentication services, this could be recognized as a marker interface
public interface AuthenticationService<C>{
    Single<UserCredentials<?>> login(C credentials);
    Single<UserCredentials<?>> register(C credentials);
}