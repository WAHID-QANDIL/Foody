package org.wahid.foody.presentation;

import org.wahid.foody.data.remote.user_auth.UserCredentials;

import io.reactivex.rxjava3.core.Single;

public interface AuthRepository<C>{

    Single<UserCredentials<?>> login(C credentials);
    Single<UserCredentials<?>> register(C credentials);
}