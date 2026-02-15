package org.wahid.foody.data.meals;

import org.wahid.foody.data.meals.remote.api.MealApiService;
import org.wahid.foody.data.meals.remote.dto.CategoryRemoteResponse;
import org.wahid.foody.data.meals.remote.dto.IngredientRemoteResponse;
import org.wahid.foody.data.meals.remote.dto.MealResponse;
import org.wahid.foody.data.core.network.RetrofitClient;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RemoteMealDatasource implements MealApiService {
    private MealApiService mealsApiService = RetrofitClient.getRetrofitServiceInstance(MealApiService.class);
    @Override
    public Single<MealResponse> getAllMeals(String firstChar) {
        return mealsApiService.getAllMeals(firstChar).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return mealsApiService.getRandomMeal().subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> getMealByCategory(String category) {
        return mealsApiService.getMealByCategory(category).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> getMealByArea(String area) {
        return mealsApiService.getMealByArea(area).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> getMealByMainIngredient(String ingredient) {
        return mealsApiService.getMealByMainIngredient(ingredient).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> getMealByName(String name) {
        return mealsApiService.getMealByName(name).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> getAllAreas(String list) {
        return mealsApiService.getAllAreas(list).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> filterByMainIngredient(String ingredient) {
        return mealsApiService.filterByMainIngredient(ingredient).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MealResponse> getMealDetailsById(String id) {
        return mealsApiService.getMealDetailsById(id).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<IngredientRemoteResponse> getAllIngredients(String list) {
        return mealsApiService.getAllIngredients(list).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<CategoryRemoteResponse> getAllCategories() {
        return mealsApiService.getAllCategories().subscribeOn(Schedulers.io());
    }
}