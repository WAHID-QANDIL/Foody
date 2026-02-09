package org.wahid.foody.data.remote.meal_service.api;

import org.wahid.foody.data.remote.meal_service.dto.CategoryRemoteResponse;
import org.wahid.foody.data.remote.meal_service.dto.IngredientRemoteResponse;
import org.wahid.foody.data.remote.meal_service.dto.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {

    @GET("search.php")
    Single<MealResponse> getAllMeals(@Query("f") String firstChar);

    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("filter.php")
    Single<MealResponse> getMealByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<MealResponse> getMealByArea(@Query("a") String area);

    @GET("filter.php")
    Single<MealResponse> getMealByMainIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Single<MealResponse> getMealByName(@Query("s") String name);

    @GET("list.php")
    Single<MealResponse> getAllAreas(@Query("a") String list);

    @GET("filter.php")
    Single<MealResponse>filterByMainIngredient(@Query("i") String ingredient);

    @GET("lookup.php")
    Single<MealResponse>getMealDetailsById(@Query("i") String id);

    @GET("list.php")
    Single<IngredientRemoteResponse>getAllIngredients(@Query("i") String list);

    @GET("categories.php")
    Single<CategoryRemoteResponse> getAllCategories();
}