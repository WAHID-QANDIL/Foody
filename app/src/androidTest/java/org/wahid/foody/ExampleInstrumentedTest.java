package org.wahid.foody;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wahid.foody.data.remote.meal_service.api.MealApiService;
import org.wahid.foody.data.remote.meal_service.dto.MealResponse;
import org.wahid.foody.data.remote.network.RetrofitClient;

import static org.junit.Assert.*;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";
    MealApiService mea = RetrofitClient.getRetrofitServiceInstance(MealApiService.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("org.wahid.foody", appContext.getPackageName());
    }


    @Test
    public void shouldReturnOneRandomMeal(){
        mea.getRandomMeal().enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                int actualSize =  response.body().getMeals().size();
                int expectedSize = 1;
                assertEquals(expectedSize, actualSize);
                assertNotNull(response.body().getMeals().getFirst());
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {

            }
        });


    }

    @Test
    public void shouldReturnListOfMeals(){
        mea.getAllMeals("b").subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MealResponse>() {
            @Override
            public void accept(MealResponse mealResponse) throws Throwable {
                Log.d(TAG, "accept: " + mealResponse.getMeals().size());
                int actualSize =  mealResponse.getMeals().size();
                int expectedSize = 58;
                assertEquals(expectedSize, actualSize);
                assertNotNull(mealResponse.getMeals().getFirst());
            }

        });


    }

}