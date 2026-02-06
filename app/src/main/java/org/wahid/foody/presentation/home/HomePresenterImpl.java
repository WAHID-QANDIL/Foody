package org.wahid.foody.presentation.home;

import android.os.Bundle;
import android.util.Log;
import androidx.navigation.Navigation;

import org.wahid.foody.R;
import org.wahid.foody.data.remote.meal_service.dto.MealDto;
import org.wahid.foody.data.remote.meal_service.dto.MealResponse;
import org.wahid.foody.presentation.MealRepository;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class HomePresenterImpl implements HomePresenter{
    private HomeView view;
    private MealRepository repository;
    public static String MEAL_ID = "mealId";


    public HomePresenterImpl(HomeView view, MealRepository repositoryImpl) {
        this.view = view;
        repository = repositoryImpl;
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

    }

    @Override
    public void fetchRandomMeal() {
        repository.getRandomMeal().observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MealResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
            @Override
            public void onSuccess(@NonNull MealResponse mealResponse) {
                view.bindRandomMealIntoCard(mealResponse.getMeals().getFirst().toDomainModel());
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
    @Override
    public void fetchPopularMeals() {
        Character queryChar = (char) new Random().nextInt(97, 122);


        repository.getAllMeals(queryChar.toString()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MealResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: fetchPopularMeals");
            }

            @Override
            public void onSuccess(@NonNull MealResponse mealResponse) {

                List<MealDomainModel> list = mealResponse.getMeals().stream().map(MealDto::toDomainModel).toList();
                Log.d(TAG, "onSuccess: "+ list);
                view.bindPopularMealsIntoRecyclerView(list);
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
