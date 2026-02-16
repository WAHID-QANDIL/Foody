package org.wahid.foody.domain.repository;

import org.wahid.foody.domain.model.AreaDomainModel;
import org.wahid.foody.domain.model.CategoryDomainModel;
import org.wahid.foody.domain.model.IngredientDomainModel;
import org.wahid.foody.domain.model.MealDomainModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepository {

    // Remote
    Single<List<MealDomainModel>>getAllMeals(String firstChar);
    Single<List<MealDomainModel>>getMealsByArea(String area);
    Single<List<MealDomainModel>>getMealByMainIngredient(String ingredient);
    Single<List<MealDomainModel>>filterByMainIngredient(String ingredient);
    Single<MealDomainModel>getRandomMeal();
    Single<List<MealDomainModel>>getMealByCategory(String category);
    Single<MealDomainModel>getMealByName(String name);
    Single<MealDomainModel>getMealDetailsById(String id);
    Single<List<CategoryDomainModel>>getAllCategories();
    Single<List<AreaDomainModel>>getAllAreas(String list);
    Single<List<IngredientDomainModel>>getAllIngredients(String list);



    // Local
    Flowable<List<MealDomainModel>> getAllLocalMeals(String userId);
    Completable insertANewMeal(MealDomainModel meal);
    Completable deleteMealById(MealDomainModel meal);

}