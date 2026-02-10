package org.wahid.foody.presentation.search_details;

import org.wahid.foody.presentation.BaseView;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.List;

public interface SearchDetailsView extends BaseView {

    void updateAdapterList(List<MealDomainModel> items);

    void showFilterDialog(String[] categories, String[] countries, String[] ingredients, SearchFilterQuery currentQuery);

    void showLoading();

    void hideLoading();

    void showError(String message);

}
