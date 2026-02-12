package org.wahid.foody.presentation.favorite;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.wahid.foody.databinding.FragmentFavoriteBinding;
import org.wahid.foody.presentation.favorite.favorite_recuclerview_adaapter.FavoriteRecyclerViewAdapter;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.ApplicationDependencyRepository;

import java.util.Collections;
import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteView  {
    private static final String TAG = "FavoriteFragment";
    private FavoriteRecyclerViewAdapter adapter;
    private FragmentFavoriteBinding binding;
    private List<MealDomainModel> items;
    private FavoritePresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        items = Collections.emptyList();
        adapter = new FavoriteRecyclerViewAdapter(items, s -> {
            presenter.onMealClicked(s);
            return null;
        });
        binding = FragmentFavoriteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new FavoritePresenterImpl(this, ApplicationDependencyRepository.localRepository, ApplicationDependencyRepository.firestoreRepository);
        presenter.onFragmentCreated();
        Log.d(TAG, "onViewCreated: ");
        binding.rvFavorites.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.rvFavorites.setAdapter(adapter);
        adapter.setOnRemoveClicked(mealDomainModel -> {
            presenter.onRemoveItem(mealDomainModel);
            return null;
        });

        binding.btnSync.setOnClickListener(v -> presenter.onSyncClicked());
        binding.btnLoadFromCloud.setOnClickListener(v -> presenter.onLoadFromCloudClicked());


    }

    @Override
    public void updateListItems(List<MealDomainModel> items) {
        Log.d(TAG, "updateListItems: "+ items);
        adapter.setItemsAndUpdate(items);
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
        binding = null;
    }
}