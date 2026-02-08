package org.wahid.foody.presentation.details;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.wahid.foody.data.MealRepositoryImpl;
import org.wahid.foody.data.remote.meal_service.RemoteMealDatasource;
import org.wahid.foody.data.remote.meal_service.dto.Ingredient;
import org.wahid.foody.databinding.FragmentDetailsBinding;
import org.wahid.foody.presentation.details.ingredient_recycler_view_adapter.IngredientRecyclerViewModel;
import org.wahid.foody.presentation.details.ingredient_recycler_view_adapter.IngredientsRecyclerViewAdapter;
import org.wahid.foody.presentation.details.instructions_recycler_view_adapter.InstructionsRecyclerViewAdapter;
import org.wahid.foody.presentation.home.HomePresenterImpl;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.ImageLoader;
import java.util.List;
public class DetailsFragment extends Fragment implements DetailsView {

    private static final String TAG = "DetailsFragment";
    private FragmentDetailsBinding binding;
    private IngredientsRecyclerViewAdapter ingredientsRecyclerViewAdapter;
    private InstructionsRecyclerViewAdapter instructionsRecyclerViewAdapter;
    private DetailsPresenter presenter;
    private YouTubePlayerView youTubePlayerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredientsRecyclerViewAdapter = new IngredientsRecyclerViewAdapter();
        instructionsRecyclerViewAdapter = new InstructionsRecyclerViewAdapter();
        presenter = new DetailsPresenterImpl(this, new MealRepositoryImpl(new RemoteMealDatasource()));

    }

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
        presenter.onViewCreated(arguments);
        youTubePlayerView = binding.videoView;
        getLifecycle().addObserver(youTubePlayerView);
        binding.btnBack.setOnClickListener(v -> presenter.onBackClicked());
        binding.btnAddToFav.setOnClickListener(v -> presenter.onAddToFavClicked());
    }



    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Override
    public void bindReceivedMealIntoComponents(MealDomainModel mealDomainModel) {
        binding.tvMealTitle.setText(mealDomainModel.mealName());
        ImageLoader.load(binding.imgMealHero, mealDomainModel.mealImageUrl());
        List<IngredientRecyclerViewModel> mealIngredients = mealDomainModel
                .ingredients()
                .stream()
                .map(Ingredient::toRecyclerViewItem)
                .toList();
        List<String> mealInstructions = mealDomainModel.instructions();


        binding.rvIngredient.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvInstructions.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));

        ingredientsRecyclerViewAdapter.updateAndNotifyListItems(mealIngredients);
        instructionsRecyclerViewAdapter.updateAndNotifyListItems(mealInstructions);


        binding.rvIngredient.setAdapter(ingredientsRecyclerViewAdapter);
        binding.rvInstructions.setAdapter(instructionsRecyclerViewAdapter);
        binding.tvIngredientCount.setText(mealIngredients.size()+" Ingredients");
        binding.btnShare.setOnClickListener(v -> presenter.onShareClicked(mealDomainModel.sourceUrl()));
    }

    @Override
    public void prepareMediaVideoPlayer(MealDomainModel model) {

    }


    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void hideProgressIndicator() {

    }

    @Override
    public void showSharOptionDialog(Intent intent) {
        startActivity(intent);
    }
}