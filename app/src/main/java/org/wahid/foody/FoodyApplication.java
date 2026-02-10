package org.wahid.foody;

import android.app.Application;

public class FoodyApplication extends Application {
    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}