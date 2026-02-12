package org.wahid.foody.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
public class NetworkConnectivityMonitor {

    private final ConnectivityManager connectivityManager;
    private final BehaviorSubject<Boolean> networkStatusSubject;
    private ConnectivityManager.NetworkCallback networkCallback;
    private boolean isRegistered = false;

    public NetworkConnectivityMonitor(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkStatusSubject = BehaviorSubject.createDefault(isNetworkAvailable());
    }

    public boolean isNetworkAvailable() {
        if (connectivityManager == null) return false;

        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) return false;

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        if (capabilities == null) return false;

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }


    public Observable<Boolean> observeNetworkStatus() {
        return networkStatusSubject.distinctUntilChanged();
    }

    public void startMonitoring() {
        if (isRegistered || connectivityManager == null) return;

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                networkStatusSubject.onNext(true);
            }

            @Override
            public void onLost(@NonNull Network network) {
                networkStatusSubject.onNext(false);
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                boolean hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                networkStatusSubject.onNext(hasInternet);
            }

            @Override
            public void onUnavailable() {
                networkStatusSubject.onNext(false);
            }
        };

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        isRegistered = true;
    }

    public void stopMonitoring() {
        if (!isRegistered || connectivityManager == null || networkCallback == null) return;

        try {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        } catch (IllegalArgumentException ignored) {
        }
        isRegistered = false;
    }
}

