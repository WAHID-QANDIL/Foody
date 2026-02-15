package org.wahid.foody.presentation.plan;

import org.wahid.foody.presentation.BaseView;
import org.wahid.foody.domain.model.MealDomainModel;

import java.util.List;

public interface PlanView extends BaseView {
    void displayBreakfastMeals(List<MealDomainModel> meals);
    void displayLunchMeals(List<MealDomainModel> meals);
    void displayDinnerMeals(List<MealDomainModel> meals);
    void updateSelectedDateDisplay(String formattedDate);
    void showEmptyState(String mealType);
    void hideEmptyState(String mealType);
}