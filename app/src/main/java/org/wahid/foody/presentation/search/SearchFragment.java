package org.wahid.foody.presentation.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wahid.foody.databinding.FragmentSearchBinding;
import org.wahid.foody.presentation.search.category_list_adapter.CategoriesRecyclerViewListAdapter;
import org.wahid.foody.presentation.search.category_list_adapter.CategoryRecyclerViewListItem;
import org.wahid.foody.presentation.search.countries_list_adapter.CountriesRecyclerViewAdapter;
import org.wahid.foody.presentation.search.countries_list_adapter.CountryListItem;
import org.wahid.foody.utils.ApplicationDependencyRepository;

import java.util.List;

public class SearchFragment extends Fragment implements SearchView {

    private SearchPresenter presenter;
    private FragmentSearchBinding binding;
    private CategoriesRecyclerViewListAdapter categoriesRecyclerViewListAdapter;
    private CountriesRecyclerViewAdapter countriesRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        categoriesRecyclerViewListAdapter = new CategoriesRecyclerViewListAdapter();
        countriesRecyclerViewAdapter = new CountriesRecyclerViewAdapter();
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        presenter = new SearchPresenterImpl(this, ApplicationDependencyRepository.repository);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.rvCategories.setLayoutManager(linearLayoutManager);
        binding.rvCategories.setAdapter(categoriesRecyclerViewListAdapter);
        binding.rvCountries.setLayoutManager( new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvCountries.setAdapter(countriesRecyclerViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onFragmentViewCreated();
    }

    @Override
    public void updateCategoriesListItems(List<CategoryRecyclerViewListItem> items) {
        categoriesRecyclerViewListAdapter.updateAndNotify(items);
    }

    @Override
    public void updateCountriesListItems(List<CountryListItem> items) {
        countriesRecyclerViewAdapter.updateAndNotify(items);
    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void hideProgressIndicator() {

    }
}