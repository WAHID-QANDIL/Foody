package org.wahid.foody.presentation.favorite;

import org.wahid.foody.domain.model.MealDomainModel;

public interface FavoritePresenter {
    void onMealClicked(String mealId);
    void onFragmentCreated();
    void onRemoveItem(MealDomainModel mealDomainModel);
    void onSyncClicked();
    void onLoadFromCloudClicked();
}