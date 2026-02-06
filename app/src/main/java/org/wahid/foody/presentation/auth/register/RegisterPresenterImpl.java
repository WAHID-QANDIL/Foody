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
import org.wahid.foody.presentation.auth.login.LoginFragment;
import org.wahid.foody.presentation.navigation.FirebaseUserNavArgument;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RegisterPresenterImpl implements RegisterPresenter {

    private static final String TAG = "RegisterPresenterImpl";
    private RegisterView view;


    public RegisterPresenterImpl(RegisterView view) {
        this.view = view;
    }


    @Override
    public void onRegisterButtonClicked(String email, String password) {
        view.showProgressIndicator();
        AuthRepositoryImpl authRepository = new AuthRepositoryImpl(AuthenticationServiceType.EMAIL_PASSWORD);
        if (!email.isEmpty() && !password.isEmpty()){
            EmailPasswordCredentials credentials = new EmailPasswordCredentials(email, password);
            authRepository.register(credentials, new OnAuthenticatedCallBack() {
                @Override
                public void onSuccess(UserCredentials userCredentials) {
                    view.showSuccessDialog(((RegisterFragment) view).requireActivity(), "Congrats User: " + userCredentials.username() + " has registered successfully.", new Runnable() {
                        @Override
                        public void run() {
                            Navigation.findNavController(((RegisterFragment)view).requireView()).navigate(R.id.action_registerFragment_to_homeFragment);
                        }
                    });
                    //TODO need to add navigate back to login with these credentials to login, and then navigate home.
                }

                @Override
                public void onFail(Throwable throwable) {
                    view.showErrorDialog(((RegisterFragment) view).requireActivity(), throwable.getMessage(), new Runnable() {
                        @Override
                        public void run() {

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
        handleFacebookAccessToken(token);
    }

    @Override
    public void onRegisterWithGoogleResult(GetCredentialResponse response) {
        Credential responseCredential = response.getCredential();

        if (responseCredential instanceof CustomCredential customCredential
                && responseCredential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            // Create Google ID Token
            Bundle credentialData = customCredential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        } else {
            Log.w(TAG, "Credential is not of type Google ID!");
        }


    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth firebaseAuth = FirebaseClient.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(((RegisterFragment)view).requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        view.showSuccessDialog(((RegisterFragment) view).requireActivity(), "Congrats User: " + user.getDisplayName() + " has registered successfully.", new Runnable() {
                            @Override
                            public void run() {


                                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.fragment_login, true).build();
                                Bundle bundle = new Bundle();
                                Log.d(TAG, "run: " + user);
                                FirebaseUserNavArgument firebaseUserNavArgument = new FirebaseUserNavArgument();
                                firebaseUserNavArgument.setEmail(user.getEmail());
                                firebaseUserNavArgument.setUsername(user.getDisplayName());
                                firebaseUserNavArgument.setImageUrl(
                                        user.getPhotoUrl() != null
                                                ? user.getPhotoUrl().toString()
                                                : null
                                );
                                bundle.putParcelable("user", firebaseUserNavArgument );
                                navigateToHomeScreen(bundle,navOptions);
                            }
                        });
                        Log.d(TAG, "firebaseAuthWithGoogle: " + user);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        onError(Objects.requireNonNull(task.getException()));
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        FirebaseAuth firebaseAuth = FirebaseClient.getInstance();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(((RegisterFragment)view).requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential:success" + Objects.requireNonNull(user).getDisplayName());
                            view.showSuccessDialog(((RegisterFragment) view).requireActivity(), "Congrats User: " + user.getDisplayName() + " has registered successfully.", new Runnable() {
                                @Override
                                public void run() {


                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            view.showErrorDialog(((RegisterFragment) view).requireActivity(), Objects.requireNonNull(task.getException()).getMessage(), new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onError(Throwable throwable) {
            view.showErrorDialog(((RegisterFragment) view).requireActivity(), throwable.getMessage(), new Runnable() {
                @Override
                public void run() {

                }
            });
    }

    private void navigateToHomeScreen(Bundle bundle, NavOptions options){
        Navigation.findNavController(((RegisterFragment)view).requireView()).navigate(R.id.action_registerFragment_to_homeFragment,bundle,options);
    }

}