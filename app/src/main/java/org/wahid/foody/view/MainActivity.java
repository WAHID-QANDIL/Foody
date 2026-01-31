package org.wahid.foody.view;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.wahid.foody.R;
import org.wahid.foody.domain.remote.MealResponse;
import org.wahid.foody.domain.remote.MealsApiService;
import org.wahid.foody.domain.remote.RetrofitClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = ContextCompat.getColor(this, R.color.primary);
        EdgeToEdge.enable(this,
                SystemBarStyle.light(
                        color,
                        color
                ),
                SystemBarStyle.light(
                        color,
                        color
                )
        );
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        // TODO(the api has been tested and it works perfectly now)
        new Handler(Looper.myLooper()).post(new Runnable() {

            @Override
            public void run() {
                Log.d("TAG", "RUN: "+ Thread.currentThread().getName());
                MealsApiService mealsApiService = RetrofitClient.getRetrofitServiceInstance();
                mealsApiService.getAllMeals("b").enqueue(new Callback<MealResponse>() {

                    @Override
                    public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                        Log.d("TAG", "onResponse: "+ Thread.currentThread().getName());
                        Log.d("TAG", "onSuccess: "+response.message());
                    }

                    @Override
                    public void onFailure(Call<MealResponse> call, Throwable t) {
                        Log.d("TAG", "onFailure: "+t.getMessage());
                    }
                }) ;}
        });


    }

    public void setOnApplyWindowInsets(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}