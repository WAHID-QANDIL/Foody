package org.wahid.foody.presentation.search;

public interface SearchPresenter {
    void onFragmentViewCreated();
    void onChipSelected(int chipType/*1 -> ingredient, 2 -> Category, 3 -> Country  */);
    void onCategoryItemClicked();
    void onCountryItemClicked();
    void onSearchTextChange(String query);

}