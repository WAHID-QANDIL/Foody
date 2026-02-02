package org.wahid.foody.data.remote.user_auth;

import org.wahid.foody.data.remote.user_auth.firebase.FirebaseUserAuthenticator;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;
import org.wahid.foody.presentation.auth.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {

    @Override
    public void login(String username, String password, OnAuthenticatedCallBack callBack) {
        FirebaseUserAuthenticator.loginWithEmailPassword(username, password, callBack);
    }

    @Override
    public void register(String username, String password, OnAuthenticatedCallBack callBack) {
        FirebaseUserAuthenticator.registerANewUser(username, password, callBack);
    }
}