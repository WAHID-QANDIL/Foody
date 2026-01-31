package org.wahid.foody.domain.remote;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MealsApiService {

    @GET("search.php?f=a")
    Call<MealResponse> getAllMeals();

}
