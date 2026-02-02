package org.wahid.foody.presentation.auth.register;

public interface RegisterView {
    void showProgressIndicator();
    void hideProgressIndicator();
    void showErrorDialog(RegisterView view, String errorMessage);
    void showLoggedInSuccessfullyDialog(RegisterView view);
}