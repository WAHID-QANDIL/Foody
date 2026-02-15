package org.wahid.foody.presentation.plan;

import org.wahid.foody.domain.model.MealDomainModel;

public interface PlanPresenter {
    void onDateSelected(String date);
    void fetchMealsForDate(String date);
    void removeMeal(String date, String mealId, String mealType);
    void onMealClicked(MealDomainModel meal);
    void onDestroy();
}