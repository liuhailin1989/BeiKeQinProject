package com.android.backchina.utils;

import android.content.pm.PackageManager;

import com.android.backchina.base.BaseApplication;

public class TDevice {
    
    
    public static int getVersionCode(){
        int versionCode = 0;
        try {
            versionCode = BaseApplication
                    .context()
                    .getPackageManager()
                    .getPackageInfo(BaseApplication.context().getPackageName(),
                            0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }
}
