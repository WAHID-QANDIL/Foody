package org.wahid.foody.data.remote.user_auth.firebase.facebook_auth_service;

import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;

public class FirebaseAuthWithFacebook implements AuthenticationWithFacebook {

    @Override
    public void login(FacebookCredentials credentials, OnAuthenticatedCallBack callBack) {
        // TODO: Implement Facebook sign-in with Firebase
    }

    @Override
    public void register(FacebookCredentials credentials, OnAuthenticatedCallBack callBack) {
        // For Facebook, register and login are essentially the same
        login(credentials, callBack);
    }
}
