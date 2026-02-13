package org.wahid.foody.presentation.search_details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.wahid.foody.databinding.FragmentSearchDetailsBinding;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.Collections;
import java.util.List;


public class SearchDetailsFragment extends Fragment implements SearchDetailsView {

    private FragmentSearchDetailsBinding binding;
    private final List<MealDomainModel> items = Collections.emptyList();
    private SearchResultsRecyclerViewAdapter adapter;
    private searchDetailsPresenterImpl presenter;
    private SearchFilterQuery currentSearchQuery;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new searchDetailsPresenterImpl(this);

        // Listen for filter dialog results
        getParentFragmentManager().setFragmentResultListener(
                FilterDialogFragment.REQUEST_KEY,
                this,
                (requestKey, result) -> {
                    SearchFilterQuery filterResult = result.getParcelable(FilterDialogFragment.RESULT_KEY);
                    if (filterResult != null) {
                        currentSearchQuery = filterResult;
                        presenter.onFilterQueryChange(filterResult);
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new SearchResultsRecyclerViewAdapter();
        binding = FragmentSearchDetailsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.searchResultsRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        binding.searchResultsRecyclerview.setAdapter(adapter);
        currentSearchQuery = new SearchFilterQuery("", "", "");

        presenter.onFragmentCreated(getArguments());
        binding.icFilter.setOnClickListener(v -> presenter.onFilterClicked());

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onSearchQueryChange(s.toString());
            }
        });
        binding.btnBack.setOnClickListener(v -> presenter.onBackClicked());

        adapter.setOnItemClicked(s -> {
            presenter.onSearchResultItemClicked(s);
            return null;
        });
    }

    @Override
    public void updateAdapterList(List<MealDomainModel> items) {
        adapter.updateListItems(items);
    }

    @Override
    public void showFilterDialog(String[] categories, String[] countries, String[] ingredients, SearchFilterQuery currentQuery) {
        FilterDialogFragment dialog = FilterDialogFragment.newInstance(
                categories,
                countries,
                ingredients,
                currentQuery
        );
        dialog.show(getParentFragmentManager(), FilterDialogFragment.TAG);
    }

    @Override
    public void showLoading() {
        if (binding != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (binding != null) {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgressIndicator() {
        showLoading();
    }

    @Override
    public void hideProgressIndicator() {
        hideLoading();
    }

    @Override
    public void showError(String message) {
        showErrorDialog(requireActivity(), message, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}