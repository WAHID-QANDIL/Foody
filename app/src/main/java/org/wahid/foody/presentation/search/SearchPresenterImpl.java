package org.wahid.foody.presentation.search;

import android.os.Bundle;
import android.util.Log;

import androidx.navigation.Navigation;

import org.wahid.foody.R;
import org.wahid.foody.presentation.MealRepository;
import org.wahid.foody.presentation.model.AreaDomainModel;
import org.wahid.foody.presentation.model.CategoryDomainModel;
import org.wahid.foody.presentation.search.category_list_adapter.CategoryRecyclerViewListItem;
import org.wahid.foody.presentation.search.countries_list_adapter.CountryListItem;
import org.wahid.foody.utils.Constants;
import org.wahid.foody.utils.SearchType;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchPresenterImpl implements SearchPresenter {


    private static final String TAG = "SearchPresenterImpl";
    private MealRepository mealRepository;
    private Observable<CategoryDomainModel> categories;
    private List<CategoryDomainModel> mCategoryDomainModelList;
    private List<CountryListItem> countries;
    private List<CategoryRecyclerViewListItem> categoryRecyclerViewListItems;
    private SearchView view;


    public SearchPresenterImpl(SearchView view, MealRepository mealRepository) {
        this.mealRepository = mealRepository;
        this.view = view;
         mealRepository.getAllCategories().observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<CategoryDomainModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onSuccess(@NonNull List<CategoryDomainModel> categoryDomainModels) {
               mCategoryDomainModelList = categoryDomainModels;
               categoryRecyclerViewListItems = mCategoryDomainModelList
                        .stream()
                        .map(it-> new CategoryRecyclerViewListItem(it.categoryImageUrl(),it.categoryName()))
                        .collect(Collectors.toList());
                view.updateCategoriesListItems(categoryRecyclerViewListItems);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Override
    public void onFragmentViewCreated() {
        mealRepository.getAllAreas("list").observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<AreaDomainModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<AreaDomainModel> areaDomainModels) {
                countries = areaDomainModels.stream().map(it-> new CountryListItem(it.flagImageUrl(),it.name())).collect(Collectors.toList());
                Log.d(TAG, "onSuccess: "+countries);
                view.updateCountriesListItems(countries);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });


    }

    @Override
    public void onCategoryItemClicked(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SEARCH_QUERY_BUNDLE_KEY,name);
        bundle.putString(Constants.SEARCH_TYPE_BUNDLE_KEY, SearchType.BY_CATEGORY.name());
        Navigation.findNavController(((SearchFragment)view).requireView()).navigate(R.id.action_searchFragment_to_searchDetailsFragment,bundle,null,null);
    }

    @Override
    public void onCountryItemClicked(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SEARCH_QUERY_BUNDLE_KEY,name);
        bundle.putString(Constants.SEARCH_TYPE_BUNDLE_KEY, SearchType.BY_COUNTRY.name());
        Navigation.findNavController(((SearchFragment)view).requireView()).navigate(R.id.action_searchFragment_to_searchDetailsFragment,bundle,null,null);
    }

    @Override
    public void onBackClicked() {
        Navigation.findNavController(((SearchFragment)view).requireView()).navigateUp();
    }
}