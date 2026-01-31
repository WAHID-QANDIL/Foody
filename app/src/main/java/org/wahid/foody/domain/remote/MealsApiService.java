package org.wahid.foody.domain.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealsApiService {

    @GET("search.php")
    Call<MealResponse> getAllMeals(@Query("f") String firstChar);

}
