package org.wahid.foody.presentation.auth.register;

import androidx.credentials.GetCredentialResponse;

import com.facebook.AccessToken;

public interface RegisterPresenter {
    void onRegisterButtonClicked(String email, String password);
    void onBackToLogin();
    void onRegisterWithGoogleClicked();
    void onRegisterWithFacebookClicked();
    void onRegisterWithFacebookResult(AccessToken token);
    void onRegisterWithGoogleResult(GetCredentialResponse response);
    void onError(Throwable throwable);
}