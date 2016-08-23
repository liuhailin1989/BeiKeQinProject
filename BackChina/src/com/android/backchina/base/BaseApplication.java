package com.android.backchina.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application{
    
    private static String PREF_NAME = "creativelocker.pref";
    
    static Context sContext;
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        sContext = getApplicationContext();
    }
    
    public static synchronized BaseApplication context() {
        return (BaseApplication) sContext;
    }
}
