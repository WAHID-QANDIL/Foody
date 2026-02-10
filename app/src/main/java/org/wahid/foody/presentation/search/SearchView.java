package org.wahid.foody.presentation.search;

import org.wahid.foody.presentation.BaseView;
import org.wahid.foody.presentation.search.category_list_adapter.CategoryRecyclerViewListItem;
import org.wahid.foody.presentation.search.countries_list_adapter.CountryListItem;

import java.util.List;

public interface SearchView extends BaseView {
    void updateCategoriesListItems(List<CategoryRecyclerViewListItem> items);
    void updateCountriesListItems(List<CountryListItem> items);

}
