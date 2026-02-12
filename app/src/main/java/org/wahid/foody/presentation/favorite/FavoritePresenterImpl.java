package org.wahid.foody.presentation.favorite;

import static org.wahid.foody.presentation.home.HomePresenterImpl.MEAL_ID;

import android.os.Bundle;
import android.util.Log;
import androidx.navigation.Navigation;
import org.reactivestreams.Subscription;
import org.wahid.foody.R;
import org.wahid.foody.presentation.MealRepository;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.ApplicationDependencyRepository;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImpl implements FavoritePresenter {

    private static final String TAG = "FavoritePresenterImpl";
    private FavoriteView view;
    private MealRepository repository;


    public FavoritePresenterImpl(FavoriteView view){
        repository = ApplicationDependencyRepository.repository;
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
        repository.getAllLocalMeals().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<MealDomainModel>>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {
                s.request(Long.MAX_VALUE);
                Log.d(TAG, "onSubscribe: ");
            }
            @Override
            public void onNext(List<MealDomainModel> models) {
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
        repository.deleteMealById(mealDomainModel).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                view.showSuccessDialog(((FavoriteFragment) view).requireActivity(), "Meal is removed from favorite", () -> { });


//                Toast.makeText(((FavoriteFragment) view).requireActivity(), "Meal has successfully deleted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(@NonNull Throwable e) {
                view.showErrorDialog(((FavoriteFragment) view).requireActivity(), e.getMessage(), () -> { });
//                Toast.makeText(((FavoriteFragment) view).requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}