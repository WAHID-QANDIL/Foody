package org.wahid.foody.presentation.auth.login;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wahid.foody.R;
import org.wahid.foody.databinding.FragmentLoginBinding;
import org.wahid.foody.utils.ShowDialog;

import java.util.Objects;

public class LoginFragment extends Fragment implements LoginView{

    private FragmentLoginBinding binding;
    private LoginPresenter presenter;
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
        binding.loginImg.setImageResource(R.drawable.login_logo);
        binding.joinNowBtn.setOnClickListener(v -> presenter.onRegisterClicked());
        binding.loginBtn.setOnClickListener((v)->{
            String username = Objects.requireNonNull(binding.edLoginEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.edLoginPassword.getText()).toString();
            presenter.onLoginClicked(username,password);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar toolbar = Objects.requireNonNull(requireActivity()).getActionBar();
        if (toolbar!= null){
            toolbar.hide();
        }
    }

    @Override
    public void showProgressIndicator() {
        binding.loginProgressCircular.setVisibility(VISIBLE);
    }

    @Override
    public void hideProgressIndicator() {
        binding.loginProgressCircular.setVisibility(GONE);
    }

    @Override
    public void showErrorDialog(LoginView view, String errorMessage) {
      ShowDialog.show(
                ((LoginFragment)view).requireContext(),
                R.drawable.ic_error,
                "Login Failed",
                errorMessage,
                R.color.divider,
                "Ok"
        );
    }

    @Override
    public void showLoggedInSuccessfullyDialog(LoginView view) {
        ShowDialog.show(
                ((LoginFragment)view).requireContext(),
                R.drawable.ic_success,
                "Login Success",
                "Logged In",
                R.color.divider,
                "Ok"
        );

    }
}