package org.wahid.foody.presentation.auth.register;

import androidx.credentials.GetCredentialRequest;

import org.wahid.foody.presentation.BaseView;


public interface RegisterView extends BaseView {
    void showGoogleRegisterDialog(GetCredentialRequest request);
    void showFacebookRegisterDialog();
}