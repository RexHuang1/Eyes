package com.dev.rexhuang.eyes;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    private static final String TAG = "MainApplication";

    private Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = MainApplication.this;
    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }
}
