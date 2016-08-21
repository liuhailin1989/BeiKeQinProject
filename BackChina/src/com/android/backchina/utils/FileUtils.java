package com.android.backchina.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileUtils {
    // ===========================================================
    // Constants
    // ===========================================================
    public final static String BASE_APP_DIR = "lemonstore";
    
    private final static String S_BASE_APP = File.separator + BASE_APP_DIR + File.separator;
    
    private final static String CRASHLOG_DIRECTORY = "CrashLog";
    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    /**
     * 获取崩溃日至保存路径
     * @param context
     * @return
     */
    public static String getCrashLogPath(Context context) {
        String path = getSavePath(context);
        if (path != null) {
            File file = new File(path, S_BASE_APP + CRASHLOG_DIRECTORY + File.separator);
            if (!file.exists())
                file.mkdirs();
            return file.getAbsolutePath();
        }
        return null;
    }
    
    /**
     * 获取应用文件系统保存目录
     * @param context
     * @return
     */
    public static String getSavePath(Context context) {
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            file = Environment.getExternalStorageDirectory();
        else
            file = context.getFilesDir();

        if (file != null) {
            return file.toString();
        }
        return null;
    }
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
