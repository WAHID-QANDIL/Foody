package org.wahid.foody.presentation.details;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.wahid.foody.R;
import org.wahid.foody.data.meals.remote.dto.Ingredient;
import org.wahid.foody.databinding.FragmentDetailsBinding;
import org.wahid.foody.presentation.details.ingredient_recycler_view_adapter.IngredientRecyclerViewModel;
import org.wahid.foody.presentation.details.ingredient_recycler_view_adapter.IngredientsRecyclerViewAdapter;
import org.wahid.foody.presentation.details.instructions_recycler_view_adapter.InstructionsRecyclerViewAdapter;
import org.wahid.foody.presentation.home.HomePresenterImpl;
import org.wahid.foody.domain.model.MealDomainModel;
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
        presenter = new DetailsPresenterImpl(this,
                ApplicationDependencyRepository.mealRepository,
                ApplicationDependencyRepository.mealRepository,
                ApplicationDependencyRepository.firestoreRepository);

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
        binding.btnAddToPlan.setOnClickListener(v -> presenter.onAddToWeeklyPlay());

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
    public void showDatePickerDialog() {
        showDatePickerFromFragment();
    }


    @Override
    public void showProgressIndicator() {
        if (binding != null) {
            binding.progressOverlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressIndicator() {
        if (binding != null) {
            binding.progressOverlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSharOptionDialog(Intent intent) {
        startActivity(intent);
    }


    private void showDatePickerFromFragment() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dp = new DatePickerDialog(requireContext(), (view, y, m, d) -> {
            long millis;
            Calendar c = Calendar.getInstance();
            c.set(y, m, d, 0, 0, 0);
            c.set(Calendar.MILLISECOND, 0);
            millis = c.getTimeInMillis();
            // now call an AlertDialog using requireContext()
            showMealDialogFromFragment(millis);
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }

    private void showMealDialogFromFragment(long selectedDateMillis) {

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_meal_toggle, null);

        MaterialButtonToggleGroup toggleGroup =
                dialogView.findViewById(R.id.mealToggleGroup);

        // Optional: default selection
        toggleGroup.check(R.id.btnBreakfast);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Choose meal type")
                .setView(dialogView)
                .setPositiveButton("Confirm", null)
                .setNegativeButton("Cancel", (d, which) -> d.dismiss())
                .create();

        dialog.setOnShowListener(d -> {
            Button confirm = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            confirm.setOnClickListener(v -> {

                int checkedId = toggleGroup.getCheckedButtonId();
                String selectedMeal;

                if (checkedId == R.id.btnBreakfast) {
                    selectedMeal = "Breakfast";
                } else if (checkedId == R.id.btnLunch) {
                    selectedMeal = "Lunch";
                } else if (checkedId == R.id.btnDinner) {
                    selectedMeal = "Dinner";
                } else {
                    // Should never happen because selectionRequired=true
                    return;
                }

                Log.d(TAG, "Date: " + selectedDateMillis +
                        " Meal: " + selectedMeal);

                presenter.onConfirmAddPlanMeal(String.valueOf(selectedDateMillis),selectedMeal);
                dialog.dismiss();
            });
        });

        dialog.show();
    }




}