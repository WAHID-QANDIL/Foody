package org.wahid.foody.presentation.on_boarding.second_onboarding_screen;

import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;

import org.wahid.foody.R;
import org.wahid.foody.presentation.on_boarding.first_onboarding_screen.FirstOnboardingScreenFragment;

public class SecondOnBoardingScreenPresenterImpl implements SecondOnBoardingScreenPresenter {

    private SecondOnBoardingScreenView view;
    private View rootView;
    public SecondOnBoardingScreenPresenterImpl(View viewContext){
        view = new SecondOnboardingScreenFragment();
        this.rootView = viewContext;
    }
    @Override
    public void onSkip() {
        Navigation.findNavController(rootView).navigate(R.id.action_fragment_second_onboarding_screen_to_fragment_login);
        Log.d("TAG", "onClick: navigated to login");
    }

    @Override
    public void onGetStarted() {
        Navigation.findNavController(rootView).navigate(R.id.action_fragment_second_onboarding_screen_to_fragment_login);
        Log.d("TAG", "onClick: navigated to second onboarding");
    }
}