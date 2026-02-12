package org.wahid.foody.presentation.details;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.wahid.foody.R;
import org.wahid.foody.data.remote.meal_service.dto.Ingredient;
import org.wahid.foody.databinding.FragmentDetailsBinding;
import org.wahid.foody.presentation.details.ingredient_recycler_view_adapter.IngredientRecyclerViewModel;
import org.wahid.foody.presentation.details.ingredient_recycler_view_adapter.IngredientsRecyclerViewAdapter;
import org.wahid.foody.presentation.details.instructions_recycler_view_adapter.InstructionsRecyclerViewAdapter;
import org.wahid.foody.presentation.home.HomePresenterImpl;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.ApplicationDependencyRepository;
import org.wahid.foody.utils.ImageLoader;

import java.text.MessageFormat;
import java.util.List;

public class DetailsFragment extends Fragment implements DetailsView {

    private static final String TAG = "DetailsFragment";
    private FragmentDetailsBinding binding;
    private IngredientsRecyclerViewAdapter ingredientsRecyclerViewAdapter;
    private InstructionsRecyclerViewAdapter instructionsRecyclerViewAdapter;
    private DetailsPresenter presenter;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredientsRecyclerViewAdapter = new IngredientsRecyclerViewAdapter();
        instructionsRecyclerViewAdapter = new InstructionsRecyclerViewAdapter();
        presenter = new DetailsPresenterImpl(this, ApplicationDependencyRepository.remoteRepository,ApplicationDependencyRepository.localRepository);

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
        Log.d(TAG, "onViewCreated: " + mealId);
        presenter.onFragmentViewCreated(arguments);
        youTubePlayerView = binding.videoView;
        getLifecycle().addObserver(youTubePlayerView);
        binding.btnBack.setOnClickListener(v -> presenter.onBackClicked());
        binding.btnAddToFav.setOnClickListener(v -> presenter.onAddToFavClicked());

        ItemTouchHelper itemTouchHelper = getItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(binding.rvInstructions);

    }

    @NonNull
    private ItemTouchHelper getItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                instructionsRecyclerViewAdapter.getInstructions().remove(position);
                instructionsRecyclerViewAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        return itemTouchHelper;
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

        binding.rvIngredient.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvInstructions.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        ingredientsRecyclerViewAdapter.updateAndNotifyListItems(mealIngredients);
        instructionsRecyclerViewAdapter.updateAndNotifyListItems(mealInstructions);

        binding.rvIngredient.setAdapter(ingredientsRecyclerViewAdapter);
        binding.rvInstructions.setAdapter(instructionsRecyclerViewAdapter);
        binding.tvIngredientCount.setText(mealIngredients.size() + " Ingredients");
        binding.btnShare.setOnClickListener(v -> presenter.onShareClicked(mealDomainModel.sourceUrl()));
        binding.tvVideoDescription.setText(MessageFormat.format("{0}{1}", getString(R.string.watch_how_to_make_perfect), mealDomainModel.mealName()));
    }

    @Override
    public void prepareMediaVideoPlayer(String videoId) {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                Log.d(TAG, "onReady: " + videoId);
                youTubePlayer = player;
                // Use cueVideo instead of loadVideo to load the video but not autoplay
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

        // Add click listener to play video when clicked
        youTubePlayerView.setOnClickListener(v -> {
            if (youTubePlayer != null) {
                youTubePlayer.play();
            }
        });
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