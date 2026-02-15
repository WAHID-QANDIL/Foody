package org.wahid.foody.presentation.favorite;

import static org.wahid.foody.presentation.home.HomePresenterImpl.MEAL_ID;

import android.os.Bundle;
import android.util.Log;
import androidx.navigation.Navigation;

import com.google.firebase.Firebase;

import org.reactivestreams.Subscription;
import org.wahid.foody.R;
import org.wahid.foody.data.remote.user_auth.firebase.FirebaseClient;
import org.wahid.foody.presentation.FirestoreRepository;
import org.wahid.foody.presentation.MealLocalRepository;
import org.wahid.foody.presentation.model.MealDomainModel;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImpl implements FavoritePresenter {

    private static final String TAG = "FavoritePresenterImpl";
    private final FavoriteView view;
    private final MealLocalRepository localRepository;
    private final FirestoreRepository firestoreRepository;
    private List<MealDomainModel> currentMeals = new ArrayList<>();


    public FavoritePresenterImpl(FavoriteView view, MealLocalRepository localRepository, FirestoreRepository firestoreRepository){
        this.localRepository = localRepository;
        this.firestoreRepository = firestoreRepository;
        this.view = view;
    }


    @Override
    public void onMealClicked(String mealId) {
        Bundle bundle = new Bundle();
        Log.d(TAG, "onPopularMealsItemClicked: " + mealId);
        bundle.putString(MEAL_ID, mealId );
        Navigation.findNavController(((FavoriteFragment)view).requireView()).navigate(R.id.action_favoriteFragment_to_detailsFragment2,bundle);
    }

    @Override
    public void onFragmentCreated() {
        String userId = FirebaseClient.getCurrentUser().getUid();
        Log.d(TAG, "onFragmentCreated: " + userId);

        localRepository.getAllLocalMeals(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<MealDomainModel>>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {
                s.request(Long.MAX_VALUE);
                Log.d(TAG, "onSubscribe: ");
            }
            @Override
            public void onNext(List<MealDomainModel> models) {
                currentMeals = models;
                view.updateListItems(models);
                Log.d(TAG, "onNext: " + models);
            }

            @Override
            public void onError(Throwable t) {
                view.showErrorDialog(((FavoriteFragment) view).requireActivity(), t.getMessage(), () -> {});
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onRemoveItem(MealDomainModel mealDomainModel) {
        localRepository.deleteMealById(mealDomainModel).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onComplete() {
                view.showSuccessDialog(((FavoriteFragment) view).requireActivity(), "Meal is removed from favorite", () -> { });
            }
            @Override
            public void onError(@NonNull Throwable e) {
                view.showErrorDialog(((FavoriteFragment) view).requireActivity(), e.getMessage(), () -> { });
            }
        });
    }

    @Override
    public void onSyncClicked() {
        if (currentMeals.isEmpty()) {
            view.showErrorDialog(((FavoriteFragment) view).requireActivity(), "No meals to sync", () -> { });
            return;
        }

        view.showProgressIndicator();
        firestoreRepository.syncFavoriteMeals(currentMeals)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgressIndicator();
                        view.showSuccessDialog(((FavoriteFragment) view).requireActivity(),
                                "Successfully synced " + currentMeals.size() + " meals to cloud", () -> { });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideProgressIndicator();
                        view.showErrorDialog(((FavoriteFragment) view).requireActivity(), e.getMessage(), () -> { });
                    }
                });
    }

    @Override
    public void onLoadFromCloudClicked() {
        view.showProgressIndicator();
        firestoreRepository.getFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MealDomainModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull List<MealDomainModel> meals) {
                        if (meals.isEmpty()) {
                            view.hideProgressIndicator();
                            view.showErrorDialog(((FavoriteFragment) view).requireActivity(),
                                    "No favorites found in cloud", () -> { });
                            return;
                        }
                        // Insert each meal into local database
                        insertMealsToLocal(meals, 0);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideProgressIndicator();
                        view.showErrorDialog(((FavoriteFragment) view).requireActivity(), e.getMessage(), () -> { });
                    }
                });
    }

    private void insertMealsToLocal(List<MealDomainModel> meals, int index) {
        if (index >= meals.size()) {
            view.hideProgressIndicator();
            view.showSuccessDialog(((FavoriteFragment) view).requireActivity(),
                    "Successfully restored " + meals.size() + " meals from cloud", () -> { });
            return;
        }

        localRepository.insertANewMeal(meals.get(index))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        insertMealsToLocal(meals, index + 1);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideProgressIndicator();
                        view.showErrorDialog(((FavoriteFragment) view).requireActivity(),
                                "Failed to restore meal: " + e.getMessage(), () -> { });
                    }
                });
    }
}