package org.wahid.foody.presentation.auth.register;

import androidx.navigation.Navigation;

import org.wahid.foody.data.remote.user_auth.AuthRepositoryImpl;
import org.wahid.foody.data.remote.user_auth.AuthenticationServiceType;
import org.wahid.foody.data.remote.user_auth.UserCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;
import org.wahid.foody.data.remote.user_auth.firebase.email_password_auth_service.EmailPasswordCredentials;

public class RegisterPresenterImpl implements RegisterPresenter {

    private RegisterView view;


    public RegisterPresenterImpl(RegisterView view) {
        this.view = view;
    }


    @Override
    public void onRegisterButtonClicked(String email, String password) {
        AuthRepositoryImpl authRepository = new AuthRepositoryImpl(AuthenticationServiceType.EMAIL_PASSWORD);
        if (!email.isEmpty() && !password.isEmpty()){
            EmailPasswordCredentials credentials = new EmailPasswordCredentials(email, password);
            authRepository.register(credentials, new OnAuthenticatedCallBack() {
                @Override
                public void onSuccess(UserCredentials userCredentials) {
                    view.showLoggedInSuccessfullyDialog(view);
                    //TODO need to add navigate back to login with these credentials to login, and then navigate home.
                }

                @Override
                public void onFail(Throwable throwable) {
                    view.showErrorDialog(view, throwable.getMessage());
                }
            });
        }else {
            view.showErrorDialog(view,"The email or password can't be empty");
        }

    }

    @Override
    public void onBackToLogin() {
        Navigation.findNavController(((RegisterFragment)view).requireView()).navigateUp();
    }

    @Override
    public void registerWithGoogleClicked() {

    }

}