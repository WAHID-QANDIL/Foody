package org.wahid.foody.presentation.search_details;

import static org.wahid.foody.presentation.home.HomePresenterImpl.MEAL_ID;

import android.os.Bundle;
import android.util.Log;

import androidx.navigation.Navigation;

import org.wahid.foody.R;
import org.wahid.foody.presentation.MealRepository;
import org.wahid.foody.presentation.model.AreaDomainModel;
import org.wahid.foody.presentation.model.CategoryDomainModel;
import org.wahid.foody.presentation.model.IngredientDomainModel;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.ApplicationDependencyRepository;
import org.wahid.foody.utils.Constants;
import org.wahid.foody.utils.SearchType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class searchDetailsPresenterImpl implements SearchDetailsPresenter {

    private static final String TAG = "searchDetailsPresenterI";
    private SearchDetailsView view;
    private MealRepository repository;
    private SearchFilterQuery filterQuery;
    private List<MealDomainModel> allMeals = new ArrayList<>();
    private String currentSearchText = "";

    // Filter options
    private String[] categories = new String[0];
    private String[] countries = new String[0];
    private String[] ingredients = new String[0];

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public searchDetailsPresenterImpl(SearchDetailsView view) {
        this.view = view;
        repository = ApplicationDependencyRepository.remoteRepository;
        filterQuery = new SearchFilterQuery(null, null, null);
        loadFilterOptions();
    }

    private void loadFilterOptions() {
        // Load categories
        Disposable categoriesDisposable = repository.getAllCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryList -> {
                            categories = categoryList.stream()
                                    .map(CategoryDomainModel::categoryName)
                                    .toArray(String[]::new);
                        },
                        error -> Log.e(TAG, "Error loading categories: " + error.getMessage())
                );
        compositeDisposable.add(categoriesDisposable);

        // Load countries
        Disposable countriesDisposable = repository.getAllAreas("list")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        areaList -> {
                            countries = areaList.stream()
                                    .map(AreaDomainModel::name)
                                    .toArray(String[]::new);
                        },
                        error -> Log.e(TAG, "Error loading countries: " + error.getMessage())
                );
        compositeDisposable.add(countriesDisposable);

        // Load ingredients
        Disposable ingredientsDisposable = repository.getAllIngredients("list")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredientList -> {
                            ingredients = ingredientList.stream()
                                    .map(IngredientDomainModel::name)
                                    .toArray(String[]::new);
                        },
                        error -> Log.e(TAG, "Error loading ingredients: " + error.getMessage())
                );
        compositeDisposable.add(ingredientsDisposable);
    }

    @Override
    public void onSearchQueryChange(String query) {
        currentSearchText = query;
        applyFilters();
    }

    private void applyFilters() {
        List<MealDomainModel> filteredList = allMeals;

        // Filter by search text
        if (!currentSearchText.isEmpty()) {
            String lowerQuery = currentSearchText.toLowerCase();
            filteredList = filteredList.stream()
                    .filter(meal -> meal.mealName() != null &&
                            meal.mealName().toLowerCase().contains(lowerQuery))
                    .collect(Collectors.toList());
        }

        view.updateAdapterList(filteredList);
    }

    @Override
    public void onSearchResultItemClicked(String itemId) {
        Bundle bundle = new Bundle();
        Log.d(TAG, "onPopularMealsItemClicked: " + itemId);
        bundle.putString(MEAL_ID, itemId);
        Navigation.findNavController(((SearchDetailsFragment) view).requireView())
                .navigate(R.id.action_searchDetailsFragment_to_detailsFragment, bundle);
    }

    @Override
    public void onFilterClicked() {
        view.showFilterDialog(categories, countries, ingredients, filterQuery);
    }

    @Override
    public void onFilterQueryChange(SearchFilterQuery query) {
        filterQuery = query;
        // Re-fetch meals based on the new filter
        fetchMealsWithFilter();
    }

    private void fetchMealsWithFilter() {
        Single<List<MealDomainModel>> listSingle = null;

        // Priority: Category > Country > Ingredient
        if (filterQuery.category != null && !filterQuery.category.isEmpty()) {
            listSingle = repository.getMealByCategory(filterQuery.category)
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (filterQuery.country != null && !filterQuery.country.isEmpty()) {
            listSingle = repository.getMealsByArea(filterQuery.country)
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (filterQuery.ingredient != null && !filterQuery.ingredient.isEmpty()) {
            listSingle = repository.getMealByMainIngredient(filterQuery.ingredient)
                    .observeOn(AndroidSchedulers.mainThread());
        }

        if (listSingle != null) {
            view.showLoading();
            listSingle.subscribe(new SingleObserver<List<MealDomainModel>>() {
                @Override
                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<MealDomainModel> models) {
                    view.hideLoading();
                    allMeals = models;
                    applyFilters();
                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                    view.hideLoading();
                    view.showError(e.getMessage());
                }
            });
        }
    }

    @Override
    public void onBackClicked() {
        Navigation.findNavController(((SearchDetailsFragment) view).requireView()).navigateUp();
    }

    @Override
    public void onFragmentCreated(Bundle arguments) {
        if (arguments != null) {
            String query = arguments.getString(Constants.SEARCH_QUERY_BUNDLE_KEY);
            String searchType = arguments.getString(Constants.SEARCH_TYPE_BUNDLE_KEY);
            SearchType searchType1 = SearchType.valueOf(searchType);
            if (searchType != null) {
                Single<List<MealDomainModel>> listSingle = null;
                switch (searchType1) {
                    case BY_CATEGORY -> {
                        filterQuery = new SearchFilterQuery(query, null, null);
                        listSingle = repository.getMealByCategory(query)
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                    case BY_COUNTRY -> {
                        filterQuery = new SearchFilterQuery(null, query, null);
                        listSingle = repository.getMealsByArea(query)
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                    case BY_INGREDIENT -> {
                        filterQuery = new SearchFilterQuery(null, null, query);
                        listSingle = repository.getMealByMainIngredient(query)
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                }
                view.showLoading();
                listSingle.subscribe(new SingleObserver<List<MealDomainModel>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<MealDomainModel> models) {
                        view.hideLoading();
                        allMeals = models;
                        view.updateAdapterList(models);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        view.hideLoading();
                        view.showError(e.getMessage());
                    }
                });
            }
        }
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}