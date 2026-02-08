package org.wahid.foody.presentation.auth.login;

import androidx.credentials.GetCredentialResponse;

import com.facebook.AccessToken;

interface LoginPresenter {
    void onLoginClicked(String username, String password);
    void onLoginWithGoogleClicked();
    void onLoginWithFacebookClicked();
    void onLoginWithFacebookResult(AccessToken token);
    void onLoginWithGoogleResult(GetCredentialResponse response);
    void onRegisterClicked();
    void onError(Throwable throwable);
}