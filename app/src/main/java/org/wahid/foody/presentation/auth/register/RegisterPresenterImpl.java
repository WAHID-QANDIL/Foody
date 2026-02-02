package org.wahid.foody.presentation.auth.register;

import androidx.navigation.Navigation;

import org.wahid.foody.R;
import org.wahid.foody.data.remote.user_auth.AuthRepositoryImpl;
import org.wahid.foody.data.remote.user_auth.UserCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.FirebaseUserAuthenticator;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;
import org.wahid.foody.presentation.auth.AuthRepository;
import org.wahid.foody.presentation.auth.login.LoginFragment;

public class RegisterPresenterImpl implements RegisterPresenter {

    private RegisterView view;
    private final AuthRepository authRepository = new AuthRepositoryImpl();

    public RegisterPresenterImpl(RegisterView view) {
        this.view = view;
    }


    @Override
    public void onRegisterButtonClicked(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()){
            authRepository.register(email, password, new OnAuthenticatedCallBack() {
                @Override
                public void onSuccess(UserCredentials credentials) {
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

}