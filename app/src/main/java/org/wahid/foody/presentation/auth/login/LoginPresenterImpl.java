package org.wahid.foody.presentation.auth.login;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.os.Bundle;
import android.util.Log;

import androidx.credentials.Credential;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.facebook.AccessToken;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import org.wahid.foody.R;
import org.wahid.foody.data.remote.user_auth.AuthRepositoryImpl;
import org.wahid.foody.data.remote.user_auth.AuthenticationServiceType;
import org.wahid.foody.data.remote.user_auth.firebase.email_password_auth_service.EmailPasswordCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.facebook_auth_service.FacebookCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.google_auth_service.GoogleCredentials;
import org.wahid.foody.presentation.auth.AuthCredentials;
import org.wahid.foody.presentation.AuthRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView view;
    private static final String TAG = "LoginPresenterImpl";
    AuthRepository<AuthCredentials> authRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
    }

    @Override
    public void onLoginClicked(String username, String password) {
        authRepository = new AuthRepositoryImpl(AuthenticationServiceType.EMAIL_PASSWORD);
        if (!username.isEmpty() && !password.isEmpty()) {
            EmailPasswordCredentials credentials = new EmailPasswordCredentials(username, password);
            disposables.add(authRepository.login(credentials)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userCredentials -> {
                                view.hideProgressIndicator();
                                view.showSuccessDialog(((LoginFragment) view).requireActivity(), "Logged In", () -> {
                                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.fragment_login, true).build();
                                    Navigation.findNavController(((LoginFragment) view).requireView()).navigate(R.id.action_fragment_login_to_homeFragment, null, navOptions, null);
                                });
                            },
                            throwable -> {
                                view.hideProgressIndicator();
                                view.showErrorDialog(((LoginFragment) view).requireActivity(), "Credentials are not correct, pls enter valid email and password", () -> {
                                    Log.d(TAG, "run: cre");
                                });
                            }
                    ));
        } else {
            view.showErrorDialog(((LoginFragment) view).requireActivity(), "The email or password can't be empty", () -> {
                //Nothing until now
            });
        }
    }

    @Override
    public void onLoginWithGoogleClicked() {
        authRepository = new AuthRepositoryImpl(AuthenticationServiceType.GOOGLE);

        GetSignInWithGoogleOption signInWithGoogleOption = new GetSignInWithGoogleOption
                .Builder(((LoginFragment)view).requireContext().getString(R.string.default_web_client_id))
                .build();
        // Create the Credential Manager request using the configuration created above
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build();
        view.showGoogleLoginDialog(request);
    }

    @Override
    public void onLoginWithFacebookClicked() {
        view.showFacebookLoginDialog();
    }

    @Override
    public void onLoginWithFacebookResult(AccessToken token) {

        authRepository = new AuthRepositoryImpl(AuthenticationServiceType.FACEBOOK);
        disposables.add(authRepository.login(new FacebookCredentials(token.getToken()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        credentials -> {
                            view.showSuccessDialog(((LoginFragment) view).requireActivity(), "LoggedIn", () -> {
                                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.fragment_login, true).build();
                                Navigation.findNavController(((LoginFragment) view).requireView()).navigate(R.id.action_fragment_login_to_homeFragment, null, navOptions, null);
                            });
                        },
                        throwable -> {
                            view.showErrorDialog(((LoginFragment) view).requireActivity(), throwable.getMessage(), () -> {
                                //TODO nothing to do in this case of failure
                            });
                        }
                ));
    }

    @Override
    public void onLoginWithGoogleResult(GetCredentialResponse response) {
        Credential responseCredential = response.getCredential();
        authRepository = new AuthRepositoryImpl(AuthenticationServiceType.GOOGLE);

        if (responseCredential instanceof CustomCredential customCredential
                && responseCredential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            // Create Google ID Token
            Bundle credentialData = customCredential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

            // Sign in to Firebase with using the token
            disposables.add(authRepository.login(new GoogleCredentials(googleIdTokenCredential.getIdToken()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            credentials -> {
                                view.showSuccessDialog(((LoginFragment) view).requireActivity(), "LoggedIn", () -> {
                                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.fragment_login, true).build();
                                    Navigation.findNavController(((LoginFragment) view).requireView()).navigate(R.id.action_fragment_login_to_homeFragment, null, navOptions, null);
                                });
                            },
                            throwable -> {
                                view.showErrorDialog(((LoginFragment) view).requireActivity(), throwable.getMessage(), () -> {
                                    //TODO nothing to do in this case of failure
                                });
                            }
                    ));
        } else {
            Log.w(TAG, "Credential is not of type Google ID!");
            onError(new Throwable("Credential is not of type Google ID!"));
        }

    }

    @Override
    public void onRegisterClicked() {
        Navigation.findNavController(((LoginFragment) view).requireView()).navigate(R.id.action_fragment_login_to_registerFragment);
    }

    @Override
    public void onError(Throwable throwable) {
        view.showErrorDialog(((LoginFragment) view).requireActivity(), throwable.getMessage(), new Runnable() {
            @Override
            public void run() {

            }
        });
    }

}