package org.wahid.foody.presentation.plan;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;
import org.wahid.foody.R;
import org.wahid.foody.data.user_auth.local.session.GuestSessionManager;
import org.wahid.foody.databinding.FragmentPlanBinding;
import org.wahid.foody.domain.model.MealDomainModel;
import org.wahid.foody.utils.ApplicationDependencyRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlanFragment extends Fragment implements PlanView {

    private static final String KEY_SELECTED_DATE = "selected_date";

    private FragmentPlanBinding binding;
    private PlanPresenter presenter;
    private PlanMealAdapter breakfastAdapter;
    private PlanMealAdapter lunchAdapter;
    private PlanMealAdapter dinnerAdapter;
    private String currentSelectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlanBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (GuestSessionManager.getInstance().isGuestMode()) {
            showGuestRestrictionDialog();
            return;
        }
        presenter = new PlanPresenterImpl(this, ApplicationDependencyRepository.firestoreRepository);

        setupAdapters();
        setupRecyclerViews();
        setupCalendarCard();

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SELECTED_DATE)) {
            currentSelectedDate = savedInstanceState.getString(KEY_SELECTED_DATE);
            updateSelectedDateDisplay(formatDate(Long.parseLong(currentSelectedDate)));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            currentSelectedDate = String.valueOf(calendar.getTimeInMillis());
            updateSelectedDateDisplay(formatDate(calendar.getTimeInMillis()));
        }

        presenter.onDateSelected(currentSelectedDate);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentSelectedDate != null) {
            outState.putString(KEY_SELECTED_DATE, currentSelectedDate);
        }
    }

    private void setupAdapters() {
        PlanMealAdapter.OnMealClickListener clickListener = meal -> {
            presenter.onMealClicked(meal);
        };

        breakfastAdapter = new PlanMealAdapter(clickListener, meal -> showDeleteConfirmation(meal, "Breakfast"));
        lunchAdapter = new PlanMealAdapter(clickListener, meal -> showDeleteConfirmation(meal, "Lunch"));
        dinnerAdapter = new PlanMealAdapter(clickListener, meal -> showDeleteConfirmation(meal, "Dinner"));
    }

    private void setupRecyclerViews() {

        RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();


        binding.rvBreakfast.setLayoutManager(new CarouselLayoutManager(new MultiBrowseCarouselStrategy()));
        binding.rvBreakfast.setHasFixedSize(true);
        binding.rvBreakfast.setItemViewCacheSize(20);
        binding.rvBreakfast.setRecycledViewPool(sharedPool);
        binding.rvBreakfast.setAdapter(breakfastAdapter);


        binding.rvLunch.setLayoutManager(new CarouselLayoutManager(new MultiBrowseCarouselStrategy()));
        binding.rvLunch.setHasFixedSize(true);
        binding.rvLunch.setItemViewCacheSize(20);
        binding.rvLunch.setRecycledViewPool(sharedPool);
        binding.rvLunch.setAdapter(lunchAdapter);


        binding.rvDinner.setLayoutManager(new CarouselLayoutManager(new MultiBrowseCarouselStrategy()));
        binding.rvDinner.setHasFixedSize(true);
        binding.rvDinner.setItemViewCacheSize(20);
        binding.rvDinner.setRecycledViewPool(sharedPool);
        binding.rvDinner.setAdapter(dinnerAdapter);
    }

    private void setupCalendarCard() {
        binding.includeCalendar.cardCalendar.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showGuestRestrictionDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Feature Not Available")
                .setMessage("Meal planning feature is not available for guest users. Please sign in to plan your meals.")
                .setPositiveButton("Sign In", (dialog, which) -> {
                    Navigation.findNavController(requireView()).navigate(R.id.fragment_login);
                })
                .setNegativeButton("Go Back", (dialog, which) -> {
                    Navigation.findNavController(requireView()).navigateUp();
                })
                .setCancelable(false)
                .show();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth, 0, 0, 0);
                    selectedCalendar.set(Calendar.MILLISECOND, 0);

                    currentSelectedDate = String.valueOf(selectedCalendar.getTimeInMillis());
                    updateSelectedDateDisplay(formatDate(selectedCalendar.getTimeInMillis()));
                    presenter.onDateSelected(currentSelectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    private void showDeleteConfirmation(MealDomainModel meal, String mealType) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Remove Meal")
                .setMessage("Are you sure you want to remove " + meal.mealName() + " from your " + mealType + " plan?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    presenter.removeMeal(currentSelectedDate, meal.mealId(), mealType);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void displayBreakfastMeals(List<MealDomainModel> meals) {
        breakfastAdapter.updateMeals(meals);
        if (meals.isEmpty()) {
            showEmptyState("Breakfast");
        } else {
            hideEmptyState("Breakfast");
        }
    }

    @Override
    public void displayLunchMeals(List<MealDomainModel> meals) {
        lunchAdapter.updateMeals(meals);
        if (meals.isEmpty()) {
            showEmptyState("Lunch");
        } else {
            hideEmptyState("Lunch");
        }
    }

    @Override
    public void displayDinnerMeals(List<MealDomainModel> meals) {
        dinnerAdapter.updateMeals(meals);
        if (meals.isEmpty()) {
            showEmptyState("Dinner");
        } else {
            hideEmptyState("Dinner");
        }
    }

    @Override
    public void updateSelectedDateDisplay(String formattedDate) {
        binding.includeCalendar.tvSelectedDate.setText(formattedDate);
    }

    @Override
    public void showEmptyState(String mealType) {
        if (binding == null) return;
        switch (mealType) {
            case "Breakfast":
                binding.rvBreakfast.setVisibility(View.GONE);
                binding.emptyBreakfast.setVisibility(View.VISIBLE);
                break;
            case "Lunch":
                binding.rvLunch.setVisibility(View.GONE);
                binding.emptyLunch.setVisibility(View.VISIBLE);
                break;
            case "Dinner":
                binding.rvDinner.setVisibility(View.GONE);
                binding.emptyDinner.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void hideEmptyState(String mealType) {
        if (binding == null) return;
        switch (mealType) {
            case "Breakfast":
                binding.rvBreakfast.setVisibility(View.VISIBLE);
                binding.emptyBreakfast.setVisibility(View.GONE);
                break;
            case "Lunch":
                binding.rvLunch.setVisibility(View.VISIBLE);
                binding.emptyLunch.setVisibility(View.GONE);
                break;
            case "Dinner":
                binding.rvDinner.setVisibility(View.VISIBLE);
                binding.emptyDinner.setVisibility(View.GONE);
                break;
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter!= null)
            presenter.onDestroy();
        binding = null;
    }
}