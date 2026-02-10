package org.wahid.foody.presentation.details;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import org.wahid.foody.presentation.MealRepository;
import org.wahid.foody.presentation.home.HomePresenterImpl;
import org.wahid.foody.presentation.model.MealDomainModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class DetailsPresenterImpl implements DetailsPresenter {

    private static final String TAG = "DetailsPresenterImpl";
    private MealRepository repository;
    private DetailsView view;
    private org.wahid.foody.presentation.model.MealDomainModel currentMeal;

    public DetailsPresenterImpl(DetailsView view, MealRepository repository) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void onBackClicked() {
        Navigation.findNavController(((DetailsFragment)view).requireView()).navigateUp();
    }

    @Override
    public void onShareClicked(String source) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, source);
        sendIntent.setType("text/plain");
        view.showSharOptionDialog(sendIntent);
    }
    @Override
    public void onAddToFavClicked() {

    }

    @Override
    public void onAddToWeeklyPlay() {

    }

    @Override
    public void onFragmentViewCreated(Bundle bundle) {
        String mealId = bundle.getString(HomePresenterImpl.MEAL_ID);
        repository.getMealDetailsById(mealId).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MealDomainModel>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }
            @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull MealDomainModel mealResponse) {
                view.bindReceivedMealIntoComponents(mealResponse);
                currentMeal = mealResponse;
                String videoId = currentMeal.mealVideoUrl().substring(currentMeal.mealVideoUrl().indexOf("=") + 1);
                view.prepareMediaVideoPlayer(videoId);
                Log.d(TAG, "onSuccess: mealResponse" + mealResponse);
                Log.d(TAG, "onSuccess: mealDomainModel" + mealResponse);
            }
            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                view.showErrorDialog(((DetailsFragment) view).requireActivity(), e.getMessage(), new Runnable() {
                    @Override
                    public void run() {
                        Navigation.findNavController(((DetailsFragment)view).requireView()).navigateUp();
                    }
                });
                Log.d(TAG, "onError: "+e);

            }
        });
    }
}
