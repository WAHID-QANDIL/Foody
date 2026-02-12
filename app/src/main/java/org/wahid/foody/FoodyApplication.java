package org.wahid.foody;

import android.app.Application;
import android.util.Log;

import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class FoodyApplication extends Application {
    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        // Global Rx error handler to avoid crashes from undeliverable exceptions
        RxJavaPlugins.setErrorHandler(new Consumer<>() {
            @Override
            public void accept(Throwable e) {
                // If Rx reports an UndeliverableException caused by a RuntimeException from
                // attempting to create a Handler in a background thread (Looper.prepare message),
                // ignore it to avoid process crash; log other errors.
                if (e instanceof UndeliverableException) {
                    Throwable cause = e.getCause();
                    if (cause instanceof RuntimeException && cause.getMessage() != null
                            && cause.getMessage().contains("Looper.prepare")) {
                        Log.w("RxErrorHandler", "Ignored Looper.prepare RuntimeException: " + cause.getMessage());
                        return;
                    }
                }
                Log.e("RxErrorHandler", "Unhandled RxJava error", e);
            }
        });
    }
}