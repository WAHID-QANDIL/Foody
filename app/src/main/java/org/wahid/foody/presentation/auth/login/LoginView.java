package org.wahid.foody.presentation.auth.login;

public interface LoginView {
    void showProgressIndicator();
    void hideProgressIndicator();
    void showErrorDialog(LoginView view, String errorMessage);
    void showLoggedInSuccessfullyDialog(LoginView view);
}