package com.dev.rexhuang.eyes;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    private static final String TAG = "MainApplication";
    private static final String APPLICATION_ID = "8978611bc69ae3ed7845c5b8706ec3a7";
    private static final String REST_API_KEY = "070a8d17108bc5831fe34747aade5c7e";
    private static Context appContext;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = MainApplication.this;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public static String getApplicationId() {
        return APPLICATION_ID;
    }

    public static String getRestApiKey() {
        return REST_API_KEY;
    }
}
