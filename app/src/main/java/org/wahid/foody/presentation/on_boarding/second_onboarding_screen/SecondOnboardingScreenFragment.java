package org.wahid.foody.presentation.on_boarding.second_onboarding_screen;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wahid.foody.R;
import org.wahid.foody.databinding.FragmentSecondOnboardingScreenBinding;
import org.wahid.foody.data.core.shardPrefs.AppPreferences;
import org.wahid.foody.utils.ApplicationDependencyRepository;

import java.util.Objects;


public class SecondOnboardingScreenFragment extends Fragment implements SecondOnBoardingScreenView {


    private FragmentSecondOnboardingScreenBinding binding;
    private SecondOnBoardingScreenPresenter presenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSecondOnboardingScreenBinding.inflate(getLayoutInflater(),container,false);
        presenter = new SecondOnBoardingScreenPresenterImpl(binding.getRoot());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.secondOnboardingImage.setImageResource(R.drawable.fried_chicken_with_vegetables_herbs_aluminum_skillet);
        binding.getStartedBtn.setOnClickListener((v -> {navigateToLogin();}));
        binding.skipTv.setOnClickListener((v -> {
            skipOnboarding();}));
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

    @Override
    public void navigateToLogin() {
        AppPreferences.getInstance(ApplicationDependencyRepository.application).setFirstLaunchCompleted();
        presenter.onGetStarted();
    }

    @Override
    public void skipOnboarding() {
        AppPreferences.getInstance(ApplicationDependencyRepository.application).setFirstLaunchCompleted();
        presenter.onSkip();
    }
}