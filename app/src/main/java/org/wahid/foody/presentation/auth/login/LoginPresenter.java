package org.wahid.foody.presentation.auth.login;

interface LoginPresenter {
    void onLoginClicked(String username, String password);
    void onLoginWithGoogleClicked();
    void onLoginWithFacebookClicked();
    void onRegisterClicked();
}