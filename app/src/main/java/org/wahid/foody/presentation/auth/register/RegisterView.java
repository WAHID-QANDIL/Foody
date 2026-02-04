package org.wahid.foody.presentation.auth.register;

import androidx.credentials.GetCredentialRequest;


public interface RegisterView {
    void showProgressIndicator();
    void hideProgressIndicator();
    void showErrorDialog(RegisterView view, String errorMessage);
    void showSuccessDialog(RegisterView view, String message);
    void showGoogleRegisterDialog(GetCredentialRequest request);
    void showFacebookRegisterDialog();
}