package org.wahid.foody.presentation.home;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;

import org.wahid.foody.R;
import org.wahid.foody.data.MealRepositoryImpl;
import org.wahid.foody.data.remote.meal_service.RemoteMealDatasource;
import org.wahid.foody.databinding.FragmentHomeBinding;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.presentation.navigation.FirebaseUserNavArgument;
import org.wahid.foody.utils.ImageLoader;
import org.wahid.foody.utils.ShowDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeFragment extends Fragment implements HomeView {
    private static final String TAG = "HomeFragment";
    private FirebaseUserNavArgument currentuser;


    public HomeFragment() {

    }

    private FragmentHomeBinding binding;
    private HomePresenter presenter = new HomePresenterImpl(this,new MealRepositoryImpl(new RemoteMealDatasource()));
    private PopularMealsRecyclerViewAdapter adapter;
    private List<RecyclerViewCardItem> items = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PopularMealsRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(new MultiBrowseCarouselStrategy());
        presenter.fetchRandomMeal();
        presenter.fetchPopularMeals();
        Log.d(TAG, "onViewCreated: called" );
        binding.popularMealRecyclerview.setLayoutManager(layoutManager);
        binding.popularMealRecyclerview.setAdapter(adapter);
        adapter.setOnItemClicked(new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {
                presenter.onPopularMealsItemClicked(s);
                return null;
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle arguments = getArguments();
//        assert arguments != null;
        currentuser = (FirebaseUserNavArgument) Objects.requireNonNull(arguments).get("user");
        ImageLoader.load(binding.imgAvatar, Objects.requireNonNull(currentuser).getImageUrl());
        binding.txtUsername.setText(currentuser.getUsername());
    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void hideProgressIndicator() {

    }

    @Override
    public void showErrorDialog(Activity view, String errorMessage, Runnable onOk) {
        ShowDialog.show(
                view,
                R.drawable.ic_error,
                "Failed",
                errorMessage,
                R.color.divider,
                "Ok",
                onOk
        );
    }

    @Override
    public void showSuccessDialog(Activity view, String message, Runnable onOk) {
        ShowDialog.show(
                view,
                R.drawable.ic_success,
                "Success",
                message,
                R.color.divider,
                "Ok",
                onOk
        );
    }

    @Override
    public void bindRandomMealIntoCard(MealDomainModel meal) {
        Glide.with(binding.getRoot()).load(meal.mealImageUrl()).into(binding.randomMealCard.imgMeal);
        binding.randomMealCard.txtMealName.setText(meal.mealName());
        binding.randomMealCard.cardMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRandomMealClicked(meal.mealId());
            }
        });
    }

    @Override
    public void bindPopularMealsIntoRecyclerView(List<MealDomainModel> models) {
        List<RecyclerViewCardItem> list = models.stream().map(it -> new RecyclerViewCardItem(it.mealId(), it.mealName(), it.area(), it.mealImageUrl())).toList();
        Log.d(TAG, "bindPopularMealsIntoRecyclerView: " + list);
        adapter.updateListItems(list);
    }
}