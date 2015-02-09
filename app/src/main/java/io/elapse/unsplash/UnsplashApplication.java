package io.elapse.unsplash;

import android.app.Application;

import io.pivotal.arca.utils.Logger;

public class UnsplashApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.setup(true, "Unsplash");
    }
}
