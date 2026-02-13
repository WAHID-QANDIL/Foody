package org.wahid.foody.presentation.details;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import org.wahid.foody.presentation.FirestoreRepository;
import org.wahid.foody.presentation.MealLocalRepository;
import org.wahid.foody.presentation.MealRepository;
import org.wahid.foody.presentation.home.HomePresenterImpl;
import org.wahid.foody.presentation.model.MealDomainModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
public class DetailsPresenterImpl implements DetailsPresenter {

    private static final String TAG = "DetailsPresenterImpl";
    private MealRepository repository;
    private MealLocalRepository localRepository;
    private DetailsView view;
    private MealDomainModel currentMeal;
    private FirestoreRepository firestoreRepository;

    public DetailsPresenterImpl(DetailsView view, MealRepository repository, MealLocalRepository localRepository, FirestoreRepository firestoreRepository) {
        this.repository = repository;
        this.localRepository = localRepository;
        this.view = view;
        this.firestoreRepository = firestoreRepository;
    }

    @Override
    public void onBackClicked() {
        Navigation.findNavController(((DetailsFragment) view).requireView()).navigateUp();
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
        view.showProgressIndicator();
        localRepository.insertANewMeal(currentMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onComplete() {
                view.hideProgressIndicator();
                view.showSuccessDialog(((DetailsFragment) view).requireActivity(), "New meal added to favorite successfully", new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.hideProgressIndicator();
                view.showErrorDialog(((DetailsFragment) view).requireActivity(), e.getMessage(), new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });
    }

    @Override
    public void onAddToWeeklyPlay() {
        view.showDatePickerDialog();
    }

    @Override
    public void onFragmentViewCreated(Bundle bundle) {
        String mealId = bundle.getString(HomePresenterImpl.MEAL_ID);
        view.showProgressIndicator();
        repository.getMealDetailsById(mealId).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MealDomainModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
            @Override
            public void onSuccess(@NonNull MealDomainModel mealResponse) {
                view.hideProgressIndicator();
                view.bindReceivedMealIntoComponents(mealResponse);
                currentMeal = mealResponse;
                String videoId = currentMeal.mealVideoUrl().substring(currentMeal.mealVideoUrl().indexOf("=") + 1);
                view.prepareMediaVideoPlayer(videoId);
                Log.d(TAG, "onSuccess: mealResponse" + mealResponse);
            }
            @Override
            public void onError(@NonNull Throwable e) {
                view.hideProgressIndicator();
                view.showErrorDialog(((DetailsFragment) view).requireActivity(), e.getMessage(), new Runnable() {
                    @Override
                    public void run() {
                        Navigation.findNavController(((DetailsFragment) view).requireView()).navigateUp();
                    }
                });
                Log.d(TAG, "onError: " + e);
            }
        });
    }
    @Override
    public void onConfirmAddPlanMeal(String selectedDate, String mealType) {
        view.showProgressIndicator();

        firestoreRepository.addANewDaySlotMeal(selectedDate, currentMeal, mealType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgressIndicator();
                        view.showSuccessDialog(
                                ((DetailsFragment) view).requireActivity(),
                                "Meal added to " + mealType + " plan for " + selectedDate,
                                new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                }
                        );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideProgressIndicator();
                        view.showErrorDialog(
                                ((DetailsFragment) view).requireActivity(),
                                "Failed to add meal: " + e.getMessage(),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                }
                        );
                    }
                });
    }
}