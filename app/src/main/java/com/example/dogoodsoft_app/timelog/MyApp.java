package com.example.dogoodsoft_app.timelog;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by dogoodsoft-app on 2017/11/8.
 */

public class MyApp extends Application{


    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);

        context = getApplicationContext();
    }


    public static   Context  getContext(){
        return context;
    }
}
