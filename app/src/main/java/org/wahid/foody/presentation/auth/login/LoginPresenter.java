package org.wahid.foody.presentation.auth.login;

interface LoginPresenter {
    void onLogin(String username, String password);
    void onRegisterClicked();
}