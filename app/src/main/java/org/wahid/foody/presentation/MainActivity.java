package org.wahid.foody.presentation;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.wahid.foody.R;
import org.wahid.foody.data.remote.meal_service.dto.MealResponse;
import org.wahid.foody.data.remote.meal_service.api.RemoteMealRepository;
import org.wahid.foody.data.remote.meal_service.api.RemoteMealResponseCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = ContextCompat.getColor(this, R.color.color_primary);
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

        RemoteMealRepository remoteMealRepository = new RemoteMealRepository();


        remoteMealRepository.getMealsByFirstChar("b", new RemoteMealResponseCallback<MealResponse, Throwable>() {
            @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
            @Override
            public void onSuccess(MealResponse response) {
                Log.d("TAG", "onResponse: "+ Thread.currentThread().getName());
                Log.d("TAG", "onSuccess: "+response.getMeals().getFirst());
            }

            @Override
            public void onFail(Throwable exception) {
                Log.d("TAG", "onFailure: "+exception.getMessage());
            }
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