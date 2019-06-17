package com.example.ocrandtranslate;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    public static Context context;


    public static Context getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

}
