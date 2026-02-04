package org.wahid.foody.data.remote.user_auth.firebase.google_auth_service;

import static androidx.core.content.ContentProviderCompat.requireContext;

import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;


public class FirebaseAuthWithGoogle implements AuthenticationWithGoogle {

    private static final String TAG = "GoogleFragment";

    @Override
    public void login(GoogleCredentials credentials, OnAuthenticatedCallBack callBack) {
        // TODO: Implement Google sign-in with Firebase

    }

    @Override
    public void register(GoogleCredentials credentials, OnAuthenticatedCallBack callBack) {
        // For Google, register and login are essentially the same
        login(credentials, callBack);
    }

}