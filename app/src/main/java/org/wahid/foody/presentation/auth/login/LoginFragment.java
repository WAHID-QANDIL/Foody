package org.wahid.foody.presentation.auth.login;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.fragment.app.Fragment;

import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.wahid.foody.R;
import org.wahid.foody.databinding.FragmentLoginBinding;
import org.wahid.foody.presentation.MainActivity;
import org.wahid.foody.utils.ShowDialog;

import java.util.Objects;

public class LoginFragment extends Fragment implements LoginView {

    private FragmentLoginBinding binding;
    private LoginPresenter presenter;
    private CredentialManager credentialManager;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private static final String TAG = "LoginFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(getLayoutInflater(), container, false);
        presenter = new LoginPresenterImpl(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCallbackManager = ((MainActivity) requireActivity()).getCallbackManager();
        binding.loginImg.setImageResource(R.drawable.login_logo);
        binding.joinNowBtn.setOnClickListener(v -> presenter.onRegisterClicked());
        binding.loginBtn.setOnClickListener((v) -> {
            String email = Objects.requireNonNull(binding.edLoginEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.edLoginPassword.getText()).toString();
            if (!email.isEmpty()) {
                if (password.length() >= 6) {
                    presenter.onLoginClicked(email, password);
                } else {
                    presenter.onError(new Throwable("Password should contains of 6 characters or more"));
                }
            } else {
                presenter.onError(new Throwable("Email can't be empty"));
            }
        });
        binding.googleLoginBtn.setOnClickListener(v -> {
            presenter.onLoginWithGoogleClicked();
        });
        binding.facebookLoginBtn.setOnClickListener(v -> {
            presenter.onLoginWithFacebookClicked();
        });
        loginButton = binding.fbLoginHidden;

        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Handle successful login
                AccessToken accessToken = loginResult.getAccessToken();
                Log.d("FB", "login success: " + accessToken.getToken());
                presenter.onLoginWithFacebookResult(loginResult.getAccessToken());
                // continue with your registration logic (fetch user profile, etc.)
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                presenter.onError(new Throwable("Login has canceled"));
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                presenter.onError(error);
            }
        });
//        initializeFacebookLoginButton();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginButton = null;
        binding = null;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (mCallbackManager != null) {
//            mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        }
    }

    @Override
    public void showProgressIndicator() {
        binding.progressCircular.setVisibility(VISIBLE);
    }

    @Override
    public void hideProgressIndicator() {
        binding.progressCircular.setVisibility(GONE);
    }

    @Override
    public void showErrorDialog(Activity view, String errorMessage, Runnable onOk) {
        ShowDialog.show(
                view,
                R.drawable.ic_error,
                "Failed",
                errorMessage,
                R.color.divider,
                "Ok",
                onOk
        );
    }

    @Override
    public void showSuccessDialog(Activity view, String message, Runnable onOk) {
        ShowDialog.show(
                view,
                R.drawable.ic_success,
                "Success",
                message,
                R.color.divider,
                "Ok",
                onOk
        );

    }

    @Override
    public void showGoogleLoginDialog(GetCredentialRequest request) {
        Log.d(TAG, "showGoogleRegisterDialog: request " + request);
        credentialManager = CredentialManager.create(binding.getRoot().getContext());
        credentialManager.getCredentialAsync(
                binding.getRoot().getContext(),
                request,
                new CancellationSignal(),
                requireActivity().getMainExecutor(),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(GetCredentialResponse getCredentialResponse) {
                        presenter.onLoginWithGoogleResult(getCredentialResponse);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        presenter.onError(e);
                        Log.e(TAG, "Couldn't retrieve user's credentials: " + e.fillInStackTrace());
                    }
                }
        );

    }

    @Override
    public void showFacebookLoginDialog() {
        binding.fbLoginHidden.performClick();
    }

//    private void initializeFacebookLoginButton() {
//        LoginButton loginButton = binding.fbLoginHidden;
//        loginButton.setPermissions("email", "public_profile");
//        loginButton.setFragment(this);
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                presenter.onLoginWithFacebookResult(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
//                presenter.onError(new Throwable("Login has canceled"));
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//                presenter.onError(error);
//            }
//        });
//    }

}