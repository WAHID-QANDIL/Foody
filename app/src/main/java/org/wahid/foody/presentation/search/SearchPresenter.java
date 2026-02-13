package org.wahid.foody.presentation.search;

public interface SearchPresenter {
    void onFragmentViewCreated();
    void onCategoryItemClicked(String categoryName);
    void onCountryItemClicked(String countryName);
    void onBackClicked();
}