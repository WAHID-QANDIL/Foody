package org.wahid.foody.presentation.auth;

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

import java.util.Objects;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(getLayoutInflater(),container,false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.registerBackToLoginBtn.setOnClickListener(listener);
        binding.registerBackBtn.setOnClickListener(listener);
        binding.registerImg.setImageResource(R.drawable.register_image);
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
            Navigation.findNavController(binding.getRoot()).navigateUp();
        }
    };
}

