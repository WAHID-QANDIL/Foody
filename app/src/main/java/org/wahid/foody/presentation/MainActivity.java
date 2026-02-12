package org.wahid.foody.presentation;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.facebook.CallbackManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.wahid.foody.R;
import org.wahid.foody.utils.NetworkConnectivityMonitor;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CallbackManager callbackManager;
    private NetworkConnectivityMonitor networkMonitor;
    private CompositeDisposable compositeDisposable;
    private Dialog offlineDialog;


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

        networkMonitor = new NetworkConnectivityMonitor(this);
        compositeDisposable = new CompositeDisposable();

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
        networkMonitor.startMonitoring();
        Disposable networkDisposable = networkMonitor.observeNetworkStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> {
                    if (isConnected) {
                        dismissOfflineDialog();
                    } else {
                        showOfflineDialog();
                    }
                });
        compositeDisposable.add(networkDisposable);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_containert);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (navController.getCurrentDestination() != null &&
                    item.getItemId() == navController.getCurrentDestination().getId()) {
                return false;
            }
            NavOptions options = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .setPopUpTo(R.id.homeFragment, false, true)
                    .build();

            try {
                navController.navigate(item.getItemId(), null, options);
                return true;
            } catch (Exception e) {
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
        });
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.homeFragment || id == R.id.searchFragment || id == R.id.favoriteFragment || id == R.id.planFragment) {
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
    @Override
    protected void onStop() {
        super.onStop();
        if (networkMonitor != null) {
            networkMonitor.stopMonitoring();
        }
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissOfflineDialog();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    private void showOfflineDialog() {
        if (isFinishing() || isDestroyed()) return;
        if (offlineDialog != null && offlineDialog.isShowing()) return;

        offlineDialog = new Dialog(this);
        offlineDialog.setContentView(R.layout.dialog_status);
        offlineDialog.setCancelable(false);
        Objects.requireNonNull(offlineDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView icon = offlineDialog.findViewById(R.id.ivStatus);
        TextView title = offlineDialog.findViewById(R.id.tvTitle);
        TextView message = offlineDialog.findViewById(R.id.tvMessage);
        Button button = offlineDialog.findViewById(R.id.btnAction);

        icon.setImageResource(R.drawable.ic_no_wifi);
        title.setText(R.string.no_internet_title);
        message.setText(R.string.no_internet_message);
        button.setText(R.string.retry);
        button.setOnClickListener(v -> {
            if (networkMonitor.isNetworkAvailable()) {
                dismissOfflineDialog();
            }
        });

        offlineDialog.show();
    }

    private void dismissOfflineDialog() {
        if (offlineDialog != null && offlineDialog.isShowing()) {
            offlineDialog.dismiss();
            offlineDialog = null;
        }
    }
}