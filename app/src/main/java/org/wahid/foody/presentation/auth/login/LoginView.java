package org.wahid.foody.presentation.auth.login;

import androidx.credentials.GetCredentialRequest;

import org.wahid.foody.presentation.BaseView;

public interface LoginView extends BaseView {
    void showGoogleLoginDialog(GetCredentialRequest request);
    void showFacebookLoginDialog();
}