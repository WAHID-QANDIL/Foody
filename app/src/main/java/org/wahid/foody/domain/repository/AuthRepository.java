package org.wahid.foody.domain.repository;

import org.wahid.foody.data.user_auth.core.UserCredentials;

import io.reactivex.rxjava3.core.Single;

public interface AuthRepository<C>{

    Single<UserCredentials<?>> login(C credentials);
    Single<UserCredentials<?>> register(C credentials);
}