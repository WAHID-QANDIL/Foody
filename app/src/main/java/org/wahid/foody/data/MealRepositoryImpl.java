package org.wahid.foody.data;

import org.wahid.foody.data.remote.meal_service.RemoteMealDatasource;
import org.wahid.foody.data.remote.meal_service.dto.MealResponse;
import org.wahid.foody.presentation.MealRepository;

import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImpl implements MealRepository {
    private RemoteMealDatasource datasource;

    public MealRepositoryImpl(RemoteMealDatasource datasource) {
        this.datasource = datasource;
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return datasource.getRandomMeal();
    }

    @Override
    public Single<MealResponse> getAllMeals(String firstChar) {
        return datasource.getAllMeals(firstChar);
    }

    @Override
    public Single<MealResponse> getMealByCategory(String category) {
        return datasource.getMealByCategory(category);
    }

    @Override
    public Single<MealResponse> getMealByArea(String area) {
        return datasource.getMealByArea(area);
    }

    @Override
    public Single<MealResponse> getMealByMainIngredient(String ingredient) {
        return datasource.getMealByMainIngredient(ingredient);
    }

    @Override
    public Single<MealResponse> getMealByName(String name) {
        return datasource.getMealByName(name);
    }

    @Override
    public Single<MealResponse> getAllAreas(String list) {
        return datasource.getAllAreas(list);
    }

    @Override
    public Single<MealResponse> filterByMainIngredient(String ingredient) {
        return datasource.filterByMainIngredient(ingredient);
    }

    @Override
    public Single<MealResponse> getMealDetailsById(String id) {
        return datasource.getMealDetailsById(id);
    }

    @Override
    public Single<MealResponse> getAllIngredients(String list) {
        return datasource.getAllIngredients(list);
    }
}