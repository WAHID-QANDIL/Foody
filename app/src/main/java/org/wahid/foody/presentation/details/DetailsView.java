package org.wahid.foody.presentation.details;

import android.content.Intent;

import org.wahid.foody.presentation.BaseView;
import org.wahid.foody.presentation.model.MealDomainModel;

public interface DetailsView extends BaseView {
    void showSharOptionDialog(Intent intent);
    void bindReceivedMealIntoComponents(MealDomainModel mealDomainModel);
    void prepareMediaVideoPlayer(MealDomainModel model);
}