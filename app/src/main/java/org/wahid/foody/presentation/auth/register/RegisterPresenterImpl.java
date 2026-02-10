package org.wahid.foody.presentation.auth.register;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigation;
import androidx.navigation.PopUpToBuilder;
import androidx.navigation.fragment.NavHostFragment;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.wahid.foody.R;
import org.wahid.foody.data.remote.user_auth.AuthRepositoryImpl;
import org.wahid.foody.data.remote.user_auth.AuthenticationServiceType;
import org.wahid.foody.data.remote.user_auth.UserCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.FirebaseClient;
import org.wahid.foody.data.remote.user_auth.firebase.OnAuthenticatedCallBack;
import org.wahid.foody.data.remote.user_auth.firebase.email_password_auth_service.EmailPasswordCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.facebook_auth_service.FacebookCredentials;
import org.wahid.foody.data.remote.user_auth.firebase.google_auth_service.GoogleCredentials;
import org.wahid.foody.presentation.auth.login.LoginFragment;
import org.wahid.foody.presentation.navigation.FirebaseUserNavArgument;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RegisterPresenterImpl implements RegisterPresenter {

    private static final String TAG = "RegisterPresenterImpl";
    private RegisterView view;

    private AuthRepositoryImpl authRepository ;
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

            authRepository.register(credentials, new OnAuthenticatedCallBack() {
                @Override
                public void onSuccess(UserCredentials userCredentials) {
                    view.hideProgressIndicator();
                    view.showSuccessDialog(((RegisterFragment) view).requireActivity(), "Logged In", new Runnable() {
                        @Override
                        public void run() {
                            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.registerFragment, true).build();
                            Navigation.findNavController(((RegisterFragment) view).requireView()).navigate(R.id.action_registerFragment_to_homeFragment, null,navOptions,null);
                        }
                    });
                }

                @Override
                public void onFail(Throwable throwable) {
                    view.hideProgressIndicator();
                    view.showErrorDialog(((RegisterFragment) view).requireActivity(), "Credentials are not correct, pls enter valid email and password", new Runnable() {
                        @Override
                        public void run() {
                            //Nothing until now
                            Log.d(TAG, "run: cre" );
                        }
                    });
                }
            });
            view.hideProgressIndicator();
        }else {
            view.hideProgressIndicator();
            view.showErrorDialog(((RegisterFragment) view).requireActivity(), "The email or password can't be empty", new Runnable() {
                @Override
                public void run() {

                }
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

        authRepository.register(new FacebookCredentials(token.getToken()), new OnAuthenticatedCallBack() {
            @Override
            public void onSuccess(UserCredentials credentials) {
                view.showSuccessDialog(((RegisterFragment) view).requireActivity(), "LoggedIn", new Runnable() {
                    @Override
                    public void run() {
                        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.registerFragment, true).build();
                        Navigation.findNavController(((RegisterFragment) view).requireView()).navigate(R.id.action_registerFragment_to_homeFragment, null,navOptions,null);
                    }
                });
            }

            @Override
            public void onFail(Throwable throwable) {
                view.showErrorDialog(((RegisterFragment) view).requireActivity(), throwable.getMessage(), new Runnable() {
                    @Override
                    public void run() {
                        //TODO nothing to do in this case of failer
                    }
                });
            }
        });
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
            authRepository.register(new GoogleCredentials(googleIdTokenCredential.getIdToken()), new OnAuthenticatedCallBack() {
                @Override
                public void onSuccess(UserCredentials credentials) {
                    view.showSuccessDialog(((RegisterFragment) view).requireActivity(), "LoggedIn", new Runnable() {
                        @Override
                        public void run() {
                            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.registerFragment, true).build();
                            Navigation.findNavController(((RegisterFragment) view).requireView()).navigate(R.id.action_registerFragment_to_homeFragment, null,navOptions,null);
                        }
                    });


                }

                @Override
                public void onFail(Throwable throwable) {
                    view.showErrorDialog(((RegisterFragment) view).requireActivity(), throwable.getMessage(), new Runnable() {
                        @Override
                        public void run() {
                            //TODO nothing to do in this case of failer
                        }
                    });
                }
            });

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