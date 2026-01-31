package org.wahid.foody.view;

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
import org.wahid.foody.databinding.FragmentSecondOnboardingScreenBinding;

import java.util.Objects;


public class SecondOnboardingScreenFragment extends Fragment {


    FragmentSecondOnboardingScreenBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSecondOnboardingScreenBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.secondOnboardingImage.setImageResource(R.drawable.fried_chicken_with_vegetables_herbs_aluminum_skillet);

        binding.getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_fragment_second_onboarding_screen_to_fragment_login);
            }
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
    public void onDestroyView() {
        super.onDestroyView();
    }
}