package org.wahid.foody.presentation;

import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface FirestoreRepository {
    Completable addANewDaySlotMeal(String date, MealDomainModel meal, String mealType);
    Completable removeADaySlotMeal(String date, String mealId, String mealType);
    Single<List<MealDomainModel>> getADaySlotMeals(String date, String mealType);
    Single<List<MealDomainModel>> getAllMealsForDate(String date);
    Completable syncFavoriteMeals(List<MealDomainModel> meals);
    Single<List<MealDomainModel>> getFavoriteMeals();
}