package org.wahid.foody.presentation.auth.register;

public interface RegisterPresenter {
    void onRegisterButtonClicked(String email, String password);
    void onBackToLogin();
    void registerWithGoogleClicked();
}
