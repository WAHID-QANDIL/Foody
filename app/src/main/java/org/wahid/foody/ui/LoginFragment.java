package org.wahid.foody.ui;

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

import java.util.Objects;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginImg.setImageResource(R.drawable.login_logo);

    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar toolbar = Objects.requireNonNull(requireActivity()).getActionBar();
        if (toolbar!= null){

            toolbar.hide();
        }
    }
}