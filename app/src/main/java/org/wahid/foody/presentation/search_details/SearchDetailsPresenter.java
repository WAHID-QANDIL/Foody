package org.wahid.foody.presentation.search_details;

import android.os.Bundle;

public interface SearchDetailsPresenter {
    void onSearchQueryChange(String query);
    void onSearchResultItemClicked(String itemId);
    void onFilterClicked();
    void onFilterQueryChange(SearchFilterQuery query);
    void onBackClicked();
    void onFragmentCreated(Bundle arguments);
}