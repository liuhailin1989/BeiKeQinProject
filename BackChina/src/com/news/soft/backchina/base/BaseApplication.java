package com.news.soft.backchina.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

public class BaseApplication extends Application{
    
    private static String PREF_NAME = "creativelocker.pref";
    
    static Context sContext;
    
    private static boolean sIsAtLeastGB;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sIsAtLeastGB = true;
        }
    }
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        sContext = getApplicationContext();
    }
    
    public static synchronized BaseApplication context() {
        return (BaseApplication) sContext;
    }
    
    /**
     * 放入已读文章列表中
     *
     */
    public static void putReadedPostList(String prefFileName, String key,
                                         String value) {
        SharedPreferences preferences = getPreferences(prefFileName);
        int size = preferences.getAll().size();
        Editor editor = preferences.edit();
        if (size >= 100) {
            editor.clear();
        }
        editor.putString(key, value);
        apply(editor);
    }
    
    /**
     * 读取是否是已读的文章列表
     *
     * @return
     */
    public static boolean isOnReadedPostList(String prefFileName, String key) {
        return getPreferences(prefFileName).contains(key);
    }
    
    public static void apply(SharedPreferences.Editor editor) {
        if (sIsAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
    
    public static SharedPreferences getPreferences(String prefName) {
        return context().getSharedPreferences(prefName,
                Context.MODE_MULTI_PROCESS);
    }
    
    public static SharedPreferences getPreferences() {
        SharedPreferences pre = context().getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }
    
    public static boolean getFromPreferences(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }
    
    public static int getFromPreferences(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }
    
    public static void setToPreferences(String key, int value) {
    	SharedPreferences preferences = getPreferences();
    	Editor editor = preferences.edit();
        editor.putInt(key, value);
        apply(editor);
    }
    
    public static void setToPreferences(String key, long value) {
    	SharedPreferences preferences = getPreferences();
    	Editor editor = preferences.edit();
        editor.putLong(key, value);
        apply(editor);
    }
    
}
