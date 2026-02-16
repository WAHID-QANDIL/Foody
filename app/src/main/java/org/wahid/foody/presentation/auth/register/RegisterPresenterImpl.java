package org.wahid.foody.presentation.auth.register;

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
import org.wahid.foody.data.user_auth.AuthRepositoryImpl;
import org.wahid.foody.data.user_auth.core.AuthenticationServiceType;
import org.wahid.foody.data.user_auth.remote.firebase.email_password_auth_service.EmailPasswordCredentials;
import org.wahid.foody.data.user_auth.remote.firebase.facebook_auth_service.FacebookCredentials;
import org.wahid.foody.data.user_auth.remote.firebase.google_auth_service.GoogleCredentials;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterPresenterImpl implements RegisterPresenter {

    private static final String TAG = "RegisterPresenterImpl";
    private RegisterView view;

    private AuthRepositoryImpl authRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public RegisterPresenterImpl(RegisterView view) {
        this.view = view;
    }


    @Override
    public void onRegisterButtonClicked(String email, String password) {
        view.showProgressIndicator();
        authRepository = new AuthRepositoryImpl(AuthenticationServiceType.EMAIL_PASSWORD);
        if (!email.isEmpty() && !password.isEmpty()){
            EmailPasswordCredentials credentials = new EmailPasswordCredentials(email, password);
            Log.d(TAG, "onRegisterButtonClicked: email: "+email);
            Log.d(TAG, "onRegisterButtonClicked: password: "+password);

            disposables.add(authRepository.register(credentials)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userCredentials -> {
                                view.hideProgressIndicator();
                                view.showSuccessDialog(((RegisterFragment) view).requireActivity(), "Logged In", () -> {
                                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.registerFragment, true).build();
                                    Navigation.findNavController(((RegisterFragment) view).requireView()).navigate(R.id.action_registerFragment_to_homeFragment, null, navOptions, null);
                                });
                            },
                            throwable -> {
                                view.hideProgressIndicator();
                                view.showErrorDialog(((RegisterFragment) view).requireActivity(), "Credentials are not correct, pls enter valid email and password", () -> {
                                    Log.d(TAG, "run: cre");
                                });
                            }
                    ));
        }else {
            view.hideProgressIndicator();
            view.showErrorDialog(((RegisterFragment) view).requireActivity(), "The email or password can't be empty", () -> {
            });
        }

    }

    @Override
    public void onBackToLogin() {
        Navigation.findNavController(((RegisterFragment)view).requireView()).navigateUp();
    }

    @Override
    public void onRegisterWithGoogleClicked() {
        view.showProgressIndicator();
        // Create the dialog configuration for the Credential Manager request
        GetSignInWithGoogleOption signInWithGoogleOption = new GetSignInWithGoogleOption
                .Builder(((RegisterFragment)view).requireContext().getString(R.string.default_web_client_id))
                .build();

        // Create the Credential Manager request using the configuration created above
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build();
        view.showGoogleRegisterDialog(request);
    }

    @Override
    public void onRegisterWithFacebookClicked() {
        view.showFacebookRegisterDialog();
        Log.d(TAG, "onRegisterWithFacebookClicked: clicked");
    }

    @Override
    public void onRegisterWithFacebookResult(AccessToken token) {

        authRepository = new AuthRepositoryImpl(AuthenticationServiceType.FACEBOOK);

        disposables.add(authRepository.register(new FacebookCredentials(token.getToken()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        credentials -> {
                            view.showSuccessDialog(((RegisterFragment) view).requireActivity(), "LoggedIn", () -> {
                                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.registerFragment, true).build();
                                Navigation.findNavController(((RegisterFragment) view).requireView()).navigate(R.id.action_registerFragment_to_homeFragment, null, navOptions, null);
                            });
                        },
                        throwable -> {
                            view.showErrorDialog(((RegisterFragment) view).requireActivity(), throwable.getMessage(), () -> {
                                //TODO nothing to do in this case of failure
                            });
                        }
                ));
    }

    @Override
    public void onRegisterWithGoogleResult(GetCredentialResponse response) {
        authRepository = new AuthRepositoryImpl(AuthenticationServiceType.GOOGLE);
        Credential responseCredential = response.getCredential();

        if (responseCredential instanceof CustomCredential customCredential
                && responseCredential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            // Create Google ID Token
            Bundle credentialData = customCredential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

            // Sign in to Firebase with using the token
            disposables.add(authRepository.register(new GoogleCredentials(googleIdTokenCredential.getIdToken()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            credentials -> {
                                view.showSuccessDialog(((RegisterFragment) view).requireActivity(), "LoggedIn", () -> {
                                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.registerFragment, true).build();
                                    Navigation.findNavController(((RegisterFragment) view).requireView()).navigate(R.id.action_registerFragment_to_homeFragment, null, navOptions, null);
                                });
                            },
                            throwable -> {
                                view.showErrorDialog(((RegisterFragment) view).requireActivity(), throwable.getMessage(), () -> {
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
    public void onError(Throwable throwable) {
            view.showErrorDialog(((RegisterFragment) view).requireActivity(), throwable.getMessage(), new Runnable() {
                @Override
                public void run() {

                }
            });
    }

}