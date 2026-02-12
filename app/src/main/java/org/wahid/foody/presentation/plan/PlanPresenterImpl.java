package org.wahid.foody.presentation.plan;

import android.os.Bundle;
import android.util.Log;

import androidx.navigation.Navigation;

import org.wahid.foody.R;
import org.wahid.foody.presentation.FirestoreRepository;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanPresenterImpl implements PlanPresenter {
    private static final String TAG = "PlanPresenterImpl";

    private final FirestoreRepository repository;
    private final PlanView view;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String currentSelectedDate;

    public PlanPresenterImpl(PlanView view, FirestoreRepository repository) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void onDateSelected(String date) {
        this.currentSelectedDate = date;
        fetchMealsForDate(date);
    }

    @Override
    public void fetchMealsForDate(String date) {
        view.showProgressIndicator();

        // Track completion of all requests
        final int[] completedRequests = {0};
        final int totalRequests = 3;

        Runnable checkAndHideProgress = () -> {
            completedRequests[0]++;
            if (completedRequests[0] >= totalRequests) {
                view.hideProgressIndicator();
            }
        };

        repository.getADaySlotMeals(date, "Breakfast")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MealDomainModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<MealDomainModel> meals) {
                        if (meals.isEmpty()) {
                            view.showEmptyState("Breakfast");
                        } else {
                            view.hideEmptyState("Breakfast");
                            view.displayBreakfastMeals(meals);
                        }
                        checkAndHideProgress.run();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Error fetching breakfast meals: " + e.getMessage());
                        view.showEmptyState("Breakfast");
                        checkAndHideProgress.run();
                    }
                });

        repository.getADaySlotMeals(date, "Lunch")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MealDomainModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<MealDomainModel> meals) {
                        if (meals.isEmpty()) {
                            view.showEmptyState("Lunch");
                        } else {
                            view.hideEmptyState("Lunch");
                            view.displayLunchMeals(meals);
                        }
                        checkAndHideProgress.run();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Error fetching lunch meals: " + e.getMessage());
                        view.showEmptyState("Lunch");
                        checkAndHideProgress.run();
                    }
                });

        repository.getADaySlotMeals(date, "Dinner")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MealDomainModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<MealDomainModel> meals) {
                        if (meals.isEmpty()) {
                            view.showEmptyState("Dinner");
                        } else {
                            view.hideEmptyState("Dinner");
                            view.displayDinnerMeals(meals);
                        }
                        checkAndHideProgress.run();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Error fetching dinner meals: " + e.getMessage());
                        view.showEmptyState("Dinner");
                        checkAndHideProgress.run();
                    }
                });
    }

    @Override
    public void removeMeal(String date, String mealId, String mealType) {
        view.showProgressIndicator();

        repository.removeADaySlotMeal(date, mealId, mealType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgressIndicator();
                        // Refresh the meals after deletion
                        fetchMealsForDate(date);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideProgressIndicator();
                        Log.e(TAG, "Error removing meal: " + e.getMessage());
                    }
                });
    }

    @Override
    public void onMealClicked(MealDomainModel meal) {
        Bundle bundle = new Bundle();
        bundle.putString("mealId", meal.mealId());
        Navigation.findNavController(((PlanFragment)view).requireView())
                .navigate(R.id.action_planFragment_to_detailsFragment, bundle);
        Log.d(TAG, "Meal clicked: " + meal.mealName());
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}