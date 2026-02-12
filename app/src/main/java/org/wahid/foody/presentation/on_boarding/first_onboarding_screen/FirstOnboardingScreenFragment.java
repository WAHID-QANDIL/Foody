package org.wahid.foody.presentation.on_boarding.first_onboarding_screen;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import org.wahid.foody.R;
import org.wahid.foody.databinding.FragmentFirstOnboardingScreenBinding;
import org.wahid.foody.utils.AppPreferences;

import java.util.Objects;

public class FirstOnboardingScreenFragment extends Fragment implements FirstOnBoardingScreenView {


    private FragmentFirstOnboardingScreenBinding binding;
    private FirstOnBoardingScreenPresenter presenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFirstOnboardingScreenBinding.inflate(getLayoutInflater(),container,false);
        presenter = new FirstOnBoardingScreenPresenterImpl(binding.getRoot());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppPreferences appPreferences = AppPreferences.getInstance(requireContext());
        if (!appPreferences.isFirstLaunch()) {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.fragment_first_onboarding_screen, true)
                    .build();
            Navigation.findNavController(view)
                    .navigate(R.id.action_fragment_first_onboarding_screen_to_fragment_login, null, navOptions);
            return;
        }

        binding.firstOnboardingImage.setImageResource(R.drawable.top_view_meals_tasty_yummy_different_pastries_dishes_brown_surface);
        binding.nextBtn.setOnClickListener((v)->{
            navigateNext();});
        binding.skipTv.setOnClickListener((v)->{
            skipOnboarding();});
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
    public void navigateNext() {
        presenter.onNext();
    }

    @Override
    public void skipOnboarding() {
        AppPreferences.getInstance(requireContext()).setFirstLaunchCompleted();
        presenter.onSkip();
    }
}