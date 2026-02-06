package org.wahid.foody.presentation.auth.login;

import androidx.navigation.NavOptions;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigation;

import org.wahid.foody.R;
import org.wahid.foody.data.remote.user_auth.AuthRepositoryImpl;
import org.wahid.foody.data.remote.user_auth.AuthenticationServiceType;
import org.wahid.foody.data.remote.user_auth.UserCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;
import org.wahid.foody.data.remote.user_auth.firebase.email_password_auth_service.EmailPasswordCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.facebook_auth_service.FacebookCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.google_auth_service.GoogleCredentials;
import org.wahid.foody.presentation.auth.AuthCredentials;
import org.wahid.foody.presentation.AuthRepository;

public class LoginPresenterImpl implements LoginPresenter{

    private LoginView view;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
    }
    @Override
    public void onLoginClicked(String username, String password) {
        AuthRepository<AuthCredentials> authRepository = new AuthRepositoryImpl(AuthenticationServiceType.EMAIL_PASSWORD);

        if (!username.isEmpty() && !password.isEmpty()){
            EmailPasswordCredentials credentials = new EmailPasswordCredentials(username, password);
            authRepository.login(credentials, new OnAuthenticatedCallBack() {
                @Override
                public void onSuccess(UserCredentials userCredentials) {
                    view.hideProgressIndicator();
                    view.showSuccessDialog(((LoginFragment) view).requireActivity(), "Logged In", new Runnable() {
                        @Override
                        public void run() {
                            onLoggedIn();
                        }
                    });
                }
                @Override
                public void onFail(Throwable throwable) {
                    view.hideProgressIndicator();
                    view.showErrorDialog(((LoginFragment) view).requireActivity(), throwable.getMessage(), new Runnable() {
                        @Override
                        public void run() {
                            //Nothing until now
                        }
                    });
                }
            });
        }else {
            view.showErrorDialog(((LoginFragment) view).requireActivity(), "The email or password can't be empty", new Runnable() {
                @Override
                public void run() {
                    //Nothing until now
                }
            });
        }
    }

    @Override
    public void onLoginWithGoogleClicked(GoogleCredentials credentials) {
        AuthRepository<AuthCredentials> authRepository = new AuthRepositoryImpl(AuthenticationServiceType.GOOGLE);
        authRepository.login(credentials, new OnAuthenticatedCallBack() {
            @Override
            public void onSuccess(UserCredentials credentials) {

            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
    }

    @Override
    public void onLoginWithFacebookClicked(FacebookCredentials credentials) {

    }


    @Override
    public void onRegisterClicked() {
        Navigation.findNavController(((LoginFragment)view).requireView()).navigate(R.id.action_fragment_login_to_registerFragment);
    }

    @Override
    public void onLoggedIn() {
        Navigation.findNavController(((LoginFragment)view).requireView()).navigate(R.id.action_fragment_login_to_homeFragment);
    }

}