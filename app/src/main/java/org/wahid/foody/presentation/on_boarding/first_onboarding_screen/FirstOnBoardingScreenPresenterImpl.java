package org.wahid.foody.presentation.on_boarding.first_onboarding_screen;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;

import org.wahid.foody.R;

public class FirstOnBoardingScreenPresenterImpl implements FirstOnBoardingScreenPresenter{

    private FirstOnBoardingScreenView view;
    private View rootView;
    public FirstOnBoardingScreenPresenterImpl(View viewContext){
        view = new FirstOnboardingScreenFragment();
        this.rootView = viewContext;
    }
    @Override
    public void onSkip() {
        Navigation.findNavController(rootView).navigate(R.id.action_fragment_first_onboarding_screen_to_fragment_login);
        Log.d("TAG", "onClick: navigated to login");
    }

    @Override
    public void onNext() {
        Navigation.findNavController(rootView).navigate(R.id.action_fragment_first_onboarding_screen_to_fragment_second_onboarding_screen);
        Log.d("TAG", "onClick: navigated to second onboarding");
    }
}
