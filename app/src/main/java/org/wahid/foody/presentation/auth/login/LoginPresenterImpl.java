package org.wahid.foody.presentation.auth.login;

import androidx.navigation.Navigation;

import org.wahid.foody.R;
import org.wahid.foody.data.remote.user_auth.AuthRepositoryImpl;
import org.wahid.foody.data.remote.user_auth.UserCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;
import org.wahid.foody.presentation.auth.AuthRepository;

public class LoginPresenterImpl implements LoginPresenter{

    private LoginView view;
    private AuthRepository authRepository;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
        authRepository = new AuthRepositoryImpl();
    }
    @Override
    public void onLogin(String username, String password) {

        if (!username.isEmpty() && !password.isEmpty()){
            authRepository.login(username, password, new OnAuthenticatedCallBack() {
                @Override
                public void onSuccess(UserCredentials credentials) {
                    view.hideProgressIndicator();
                    view.showLoggedInSuccessfullyDialog(view);
                }
                @Override
                public void onFail(Throwable throwable) {
                    view.hideProgressIndicator();
                    view.showErrorDialog(view,throwable.getMessage());
                }
            });
        }else {
            view.showErrorDialog(view,"The email or password can't be empty");
        }
    }

    @Override
    public void onRegisterClicked() {
        Navigation.findNavController(((LoginFragment)view).requireView()).navigate(R.id.action_fragment_login_to_registerFragment);
    }

}