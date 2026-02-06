package org.wahid.foody.presentation;

import org.wahid.foody.data.remote.meal_service.dto.MealResponse;

import io.reactivex.rxjava3.core.Single;

public interface MealRepository {
    Single<MealResponse> getRandomMeal              ();
    Single<MealResponse> getAllMeals                (String firstChar);
    Single<MealResponse> getMealByCategory          (String category);
    Single<MealResponse> getMealByArea              (String area);
    Single<MealResponse> getMealByMainIngredient    (String ingredient);
    Single<MealResponse> getMealByName              (String name);
    Single<MealResponse> getAllAreas                (String list);
    Single<MealResponse>filterByMainIngredient      (String ingredient);
    Single<MealResponse>getMealDetailsById          (String id);
    Single<MealResponse>getAllIngredients           (String list);
}