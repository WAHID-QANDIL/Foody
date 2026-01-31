package org.wahid.foody.domain.remote.meal_service;

import org.wahid.foody.domain.remote.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteMealRepository {
    MealsApiService mealsApiService = RetrofitClient.getRetrofitServiceInstance(MealsApiService.class);

    public void getMealsByFirstChar(String firstChar, RemoteMealResponse<MealResponse,Throwable> callback){

        mealsApiService.getAllMeals(firstChar).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                callback.onFail(t);
            }
        });

    }


}