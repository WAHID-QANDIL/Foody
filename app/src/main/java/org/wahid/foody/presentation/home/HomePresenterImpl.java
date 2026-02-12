package org.wahid.foody.presentation.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import org.wahid.foody.R;
import org.wahid.foody.presentation.MealRepository;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.AppPreferences;

import java.util.List;
import java.util.Random;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class HomePresenterImpl implements HomePresenter{
    private HomeView view;
    private MealRepository repository;
    private AppPreferences appPreferences;
    public static String MEAL_ID = "mealId";


    public HomePresenterImpl(HomeView view, MealRepository repositoryImpl) {
        this.view = view;
        repository = repositoryImpl;
        appPreferences = AppPreferences.getInstance(((HomeFragment) view).requireContext());
    }

    @Override
    public void onRandomMealClicked(String id) {
        Bundle bundle = new Bundle();
        Log.d(TAG, "onPopularMealsItemClicked: " + id);
        bundle.putString(MEAL_ID, id );
        Navigation.findNavController(((HomeFragment)view).requireView()).navigate(R.id.action_homeFragment_to_detailsFragment,bundle);
    }

    @Override
    public void onPopularMealsItemClicked(String mealId) {
        Bundle bundle = new Bundle();
        Log.d(TAG, "onPopularMealsItemClicked: " + mealId);
        bundle.putString(MEAL_ID, mealId );
        Navigation.findNavController(((HomeFragment)view).requireView()).navigate(R.id.action_homeFragment_to_detailsFragment,bundle);
    }

    @Override
    public void onProfilePictureClicked() {

    }

    @Override
    public void onShowAllClicked() {
        Navigation.findNavController(((HomeFragment)view).requireView()).navigate(R.id.action_homeFragment_to_searchFragment);
    }

    @Override
    public void fetchRandomMeal() {
        String savedMealId = appPreferences.getMealOfTheDayId();

        if (savedMealId != null) {
            repository.getMealDetailsById(savedMealId).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MealDomainModel>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
                @Override
                public void onSuccess(@NonNull MealDomainModel mealResponse) {
                    view.bindRandomMealIntoCard(mealResponse);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                   fetchNewRandomMeal();
                }
            });
        } else {
            fetchNewRandomMeal();
        }
    }

    private void fetchNewRandomMeal() {
        repository.getRandomMeal().observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MealDomainModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
            @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
            @Override
            public void onSuccess(@NonNull MealDomainModel mealResponse) {
                appPreferences.setMealOfTheDayId(mealResponse.mealId());
                view.bindRandomMealIntoCard(mealResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.showErrorDialog(((HomeFragment) view).requireActivity(), e.getMessage(), new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

    private static final String TAG = "HomePresenterImpl";
    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @Override
    public void fetchPopularMeals() {
        Character queryChar = (char) new Random().nextInt(97, 122);

        repository.getAllMeals(queryChar.toString()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MealDomainModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: fetchPopularMeals");
            }

            @Override
            public void onSuccess(@NonNull List<MealDomainModel> models) {
                view.bindPopularMealsIntoRecyclerView(models);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                    Log.d(TAG, "onError: " + e.getMessage());
                    view.showErrorDialog(((HomeFragment) view).requireActivity(), e.getMessage(), new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "run: "+ e);
                        }
                    });
            }
        });


    }
}
