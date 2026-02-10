package org.wahid.foody.presentation;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.facebook.CallbackManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.wahid.foody.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        int color = ContextCompat.getColor(this, R.color.colorPrimaryLight);
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
        setContentView(R.layout.activity_main);


        callbackManager = CallbackManager.Factory.create();
        View mainRoot = findViewById(R.id.main);
        FragmentContainerView navHost = findViewById(R.id.nav_host_fragment_containert);

        ViewCompat.setOnApplyWindowInsetsListener(mainRoot, (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars());
            int statusBarHeight = systemBars.top;

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) navHost.getLayoutParams();
            params.topMargin = statusBarHeight;
            navHost.setLayoutParams(params);
            Insets navBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars());
            v.setPadding(0, 0, 0, navBarInsets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });


    }
    public CallbackManager getCallbackManager() {
        return callbackManager;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_containert);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.homeFragment || id == R.id.searchFragment) {
                showBottomNav(bottomNavigationView);
            } else {
                hideBottomNav(bottomNavigationView);
            }
        });
    }

    private void showBottomNav(BottomNavigationView navView) {
        if (navView.getVisibility() == View.VISIBLE) return;

        navView.clearAnimation();
        navView.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        navView.setVisibility(View.VISIBLE);
                    }
                }).start();
    }
    private void hideBottomNav(BottomNavigationView navView) {
        if (navView.getVisibility() == View.GONE) return;

        navView.clearAnimation();
        navView.animate()
                .translationY(navView.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        navView.setVisibility(View.GONE);
                    }
                }).start();
    }

    public void setOnApplyWindowInsets(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}