package org.wahid.foody.presentation.auth.login;

import org.wahid.foody.data.remote.user_auth.firebase.facebook_auth_service.FacebookCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.google_auth_service.GoogleCredentials;

interface LoginPresenter {
    void onLoginClicked(String username, String password);
    void onLoginWithGoogleClicked(GoogleCredentials credentials);
    void onLoginWithFacebookClicked(FacebookCredentials credentials);
    void onRegisterClicked();
    void onLoggedIn();
}