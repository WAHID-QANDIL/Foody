package org.wahid.foody.presentation.auth.register;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wahid.foody.R;
import org.wahid.foody.databinding.FragmentRegisterBinding;
import org.wahid.foody.presentation.auth.login.LoginFragment;
import org.wahid.foody.presentation.auth.login.LoginView;
import org.wahid.foody.utils.ShowDialog;

import java.util.Objects;

public class RegisterFragment extends Fragment implements RegisterView {

    private FragmentRegisterBinding binding;
    private RegisterPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(getLayoutInflater(),container,false);
        presenter = new RegisterPresenterImpl(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.registerBackToLoginBtn.setOnClickListener(listener);
        binding.registerBackBtn.setOnClickListener(listener);
        binding.registerImg.setImageResource(R.drawable.register_image);
        binding.registerBtn.setOnClickListener((v)->{
            String email = Objects.requireNonNull(binding.edRegisterEmail.getText()).toString();
            String password = binding.edRegisterEmail.getText().toString();
            presenter.onRegisterButtonClicked(email,password);
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
    public void showErrorDialog(RegisterView view, String errorMessage) {
        ShowDialog.show(
                ((RegisterFragment)view).requireContext(),
                R.drawable.ic_error,
                "Login Failed",
                errorMessage,
                R.color.divider,
                "Ok"
        );
    }

    @Override
    public void showLoggedInSuccessfullyDialog(RegisterView view) {
        ShowDialog.show(
                ((RegisterFragment)view).requireContext(),
                R.drawable.ic_success,
                "Login Success",
                "Logged In",
                R.color.divider,
                "Ok"
        );

    }
}