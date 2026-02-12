package org.wahid.foody.presentation.home;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;
import com.google.firebase.auth.FirebaseUser;

import org.wahid.foody.R;
import org.wahid.foody.data.remote.user_auth.firebase.FirebaseClient;
import org.wahid.foody.data.remote.user_auth.session.GuestSessionManager;
import org.wahid.foody.databinding.FragmentHomeBinding;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.ApplicationDependencyRepository;
import org.wahid.foody.utils.ShowDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements HomeView {
    private static final String TAG = "HomeFragment";
    private FirebaseUser currentuser;


    public HomeFragment() {

    }

    private FragmentHomeBinding binding;
    private HomePresenter presenter;
    private PopularMealsRecyclerViewAdapter adapter;
    private List<MealsRecyclerViewCardItem> items = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PopularMealsRecyclerViewAdapter();
        presenter = new HomePresenterImpl(this, ApplicationDependencyRepository.remoteRepository);
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
        Log.d(TAG, "onViewCreated: called");
        binding.popularMealRecyclerview.setLayoutManager(layoutManager);
        binding.popularMealRecyclerview.setAdapter(adapter);
        adapter.setOnItemClicked(s -> {
            presenter.onPopularMealsItemClicked(s);
            return null;
        });
        binding.tvSeeAll.setOnClickListener(v -> presenter.onShowAllClicked());
        binding.imgAvatar.setOnClickListener(this::showProfileMenu);
    }

    private void showProfileMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                showLogoutConfirmationDialog();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_confirmation_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> performLogout())
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void performLogout() {
        FirebaseClient.signOut();

        GuestSessionManager.getInstance().clearSession();
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.app_navigation_graph, true)
                .build();
        Navigation.findNavController(requireView())
                .navigate(R.id.fragment_login, null, navOptions);
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar toolbar = Objects.requireNonNull(requireActivity()).getActionBar();
        if (toolbar != null) {
            toolbar.hide();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle arguments = getArguments();
        if (GuestSessionManager.getInstance().isGuestMode()) {
            binding.imgAvatar.setImageResource(R.drawable.place_holder_avatar);
            binding.txtUsername.setText(R.string.guest_user);
        } else {
            currentuser = FirebaseClient.getInstance().getCurrentUser();
            if (currentuser != null) {
                Glide.with(binding.imgAvatar).load(
                        Objects.requireNonNullElse(currentuser.getPhotoUrl(), "")
                ).placeholder(R.drawable.place_holder_avatar).into(binding.imgAvatar);
                String name = currentuser.getDisplayName();
                if (name != null) binding.txtUsername.setText(name);
                else binding.txtUsername.setText(R.string.amazing_chief);
            } else {
                binding.imgAvatar.setImageResource(R.drawable.place_holder_avatar);
                binding.txtUsername.setText(R.string.amazing_chief);
            }
        }
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
        binding.randomMealCard.cardMeal.setOnClickListener(v -> presenter.onRandomMealClicked(meal.mealId()));
        binding.randomMealCard.btnViewRecipe.setOnClickListener(v -> presenter.onRandomMealClicked(meal.mealId()));
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Override
    public void bindPopularMealsIntoRecyclerView(List<MealDomainModel> models) {
        List<MealsRecyclerViewCardItem> list = models.stream().map(it -> new MealsRecyclerViewCardItem(it.mealId(), it.mealName(), it.area(), it.mealImageUrl())).toList();
        Log.d(TAG, "bindPopularMealsIntoRecyclerView: " + list);
        adapter.updateListItems(list);
    }
}