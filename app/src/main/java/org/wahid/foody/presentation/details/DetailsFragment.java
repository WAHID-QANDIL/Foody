package org.wahid.foody.presentation.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wahid.foody.data.MealRepositoryImpl;
import org.wahid.foody.data.remote.meal_service.RemoteMealDatasource;
import org.wahid.foody.data.remote.meal_service.dto.MealResponse;
import org.wahid.foody.databinding.FragmentDetailsBinding;
import org.wahid.foody.presentation.MealRepository;
import org.wahid.foody.presentation.home.HomePresenterImpl;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.ImageLoader;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;


public class DetailsFragment extends Fragment {


    MealRepository repository = new MealRepositoryImpl(new RemoteMealDatasource());
    private static final String TAG = "DetailsFragment";
    private FragmentDetailsBinding binding;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetailsBinding.inflate(getLayoutInflater());

        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        assert arguments != null;
        String mealId = arguments.getString(HomePresenterImpl.MEAL_ID);
        Log.d(TAG, "onViewCreated: "+ mealId);
        repository.getMealDetailsById(mealId).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MealResponse>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull MealResponse mealResponse) {
                MealDomainModel mealDomainModel = mealResponse.getMeals().getFirst().toDomainModel();
                binding.tvMealTitle.setText(mealDomainModel.mealName());
                ImageLoader.load(binding.imgMealHero, mealDomainModel.mealImageUrl());
                Log.d(TAG, "onSuccess: mealResponse" + mealResponse);
                Log.d(TAG, "onSuccess: mealDomainModel" + mealDomainModel);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Log.d(TAG, "onError: "+e);


            }
        });






    }
}