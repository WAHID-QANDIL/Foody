package org.wahid.foody.presentation.auth.register;

import android.app.ActionBar;

import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;

import androidx.credentials.CredentialManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.fragment.app.Fragment;

import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.wahid.foody.R;
import org.wahid.foody.databinding.FragmentRegisterBinding;
import org.wahid.foody.utils.ShowDialog;

import java.util.Objects;

public class RegisterFragment extends Fragment implements RegisterView {
    private static final String TAG = "RegisterFragment";

    private CredentialManager credentialManager;
    private FragmentRegisterBinding binding;
    private RegisterPresenter presenter;
    private CallbackManager mCallbackManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(getLayoutInflater(), container, false);
        presenter = new RegisterPresenterImpl(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
        initializeFacebookLoginButton();
        binding.registerBackToLoginBtn.setOnClickListener(listener);
        binding.registerImg.setImageResource(R.drawable.register_image);

        binding.registerBtn.setOnClickListener((v) -> {
            String email = Objects.requireNonNull(binding.edRegisterEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.edRegisterPassword.getText()).toString();
            if (!email.isEmpty()) {
                if (password.length() >= 6) {
                    presenter.onRegisterButtonClicked(email, password);
                } else {
                    presenter.onError(new Throwable("Password should contains of 6 characters or more"));
                }
            } else {
                presenter.onError(new Throwable("Email can't be empty"));
            }

        });
        binding.googleRegisterBtn.setOnClickListener(v -> {
            presenter.onRegisterWithGoogleClicked();
        });
        binding.facebookRegisterBtn.setOnClickListener(v -> {
            presenter.onRegisterWithFacebookClicked();
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        ActionBar toolbar = Objects.requireNonNull(requireActivity()).getActionBar();
        if (toolbar != null) {
            toolbar.hide();
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onBackToLogin();
        }
    };

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void hideProgressIndicator() {

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
    public void showGoogleRegisterDialog(GetCredentialRequest request) {
        Log.d(TAG, "showGoogleRegisterDialog: request " + request);
        try {
            Activity activity = requireActivity();
            credentialManager = CredentialManager.create(activity);
            credentialManager.getCredentialAsync(
                    activity,
                    request,
                    new CancellationSignal(),
                    activity.getMainExecutor(),
                    new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                        @Override
                        public void onResult(GetCredentialResponse getCredentialResponse) {
                            presenter.onRegisterWithGoogleResult(getCredentialResponse);
                        }

                        @Override
                        public void onError(@NonNull GetCredentialException e) {
                            Log.e(TAG, "CredentialManager getCredentialAsync error", e);
                            presenter.onError(e);
                        }
                    }
            );
        } catch (Exception e) {
            Log.e(TAG, "Failed to start credential flow", e);
            presenter.onError(e);
        } finally {
            hideProgressIndicator();
        }
    }

    @Override
    public void showFacebookRegisterDialog() {
        binding.fbRegisterHidden.performClick();
    }

    private void initializeFacebookLoginButton(){
        LoginButton loginButton = binding.fbRegisterHidden;
        loginButton.setPermissions("email", "public_profile");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                presenter.onRegisterWithFacebookResult(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                presenter.onError(new Throwable("Login has been canceled"));
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                presenter.onError(error);
            }
        });
    }


}