package org.wahid.foody.presentation.auth.register;

import androidx.credentials.GetCredentialRequest;

import org.wahid.foody.presentation.BasePresenter;


public interface RegisterView extends BasePresenter {
    void showGoogleRegisterDialog(GetCredentialRequest request);
    void showFacebookRegisterDialog();
}