package org.wahid.foody.data.remote.meal_service.api;

import org.wahid.foody.data.remote.meal_service.dto.MealResponse;
import org.wahid.foody.data.remote.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteMealRepository {
    MealApiService mealsApiService = RetrofitClient.getRetrofitServiceInstance(MealApiService.class);

    public void getMealsByFirstChar(String firstChar, RemoteMealResponseCallback<MealResponse,Throwable> callback){

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