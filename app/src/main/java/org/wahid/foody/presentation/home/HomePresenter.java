package org.wahid.foody.presentation.home;

public interface HomePresenter {
    void onRandomMealClicked(String id);
    void onPopularMealsItemClicked(String mealId);
    void onProfilePictureClicked();
    void onShowAllClicked();
    void fetchRandomMeal();
    void fetchPopularMeals();
}