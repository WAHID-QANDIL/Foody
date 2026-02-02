package org.wahid.foody.data.remote.meal_service.api;

import org.wahid.foody.data.remote.meal_service.dto.CategoryFilterRemoteResponse;
import org.wahid.foody.data.remote.meal_service.dto.MealDto;
import org.wahid.foody.data.remote.meal_service.dto.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {

    @GET("search.php")
    Call<MealResponse> getAllMeals(@Query("f") String firstChar);

    @GET("random.php")
    Call<MealDto> getRandomMeal();

    @GET("filter.php")
    Call<CategoryFilterRemoteResponse> getMealByCategory(@Query("c") String category);
//
//
//    @GET("filter.php")
//    Call<MealDto> getMealByArea(@Query("a") String area);
//
//
//    @GET("filter.php")
//    Call<MealDto> getMealByMainIngredient(@Query("i") String ingredient);
//
//
//    @GET("filter.php")
//    Call<MealDto> getMealByName(@Query("s") String name);
//
//
//
//    @GET("list.php")
//    Call<MealDto> getAllAreas(@Query("a") String list);
}