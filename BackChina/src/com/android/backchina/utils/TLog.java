package com.android.backchina.utils;

import android.util.Log;

public class TLog {
    public static final String LOG_TAG = "BACK_CHINA";

    public static boolean DEBUG = true;
    
    public TLog() {
        
    }

    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();

        if (sts == null) {
            return null;
        }

        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(TLog.class.getName())) {
                continue;
            }

//            return "["+"tid:" + Thread.currentThread().getId() + "]"
//                  +"["+"class:" + st.getFileName() + "]"
//                  +"["+"method:" + st.getMethodName() + "]"
//                  +"["+"line:" + st.getLineNumber() + "]";
            return "["+"tid:" + Thread.currentThread().getId() + "]"
            +"["+ st.getFileName() + "]"
            +"["+ st.getMethodName() + "]"
            +"["+ st.getLineNumber() + "]";
        }

        return null;
    }
    
    private static String error(Exception ex) {
        StringBuffer sb = new StringBuffer();
        String name = getFunctionName();
        StackTraceElement[] sts = ex.getStackTrace();

        if (name != null) {
            sb.append(name + ex + "\r\n");
        } else {
            sb.append(ex + "\r\n");
        }

        if (sts != null && sts.length > 0) {
            for (StackTraceElement st : sts) {
                if (st != null) {
                    sb.append("[ ")
                    .append(st.getFileName())
                    .append("]")
                    .append("[")
                    .append(st.getMethodName())
                    .append("]")
                    .append("[")
                    .append(st.getLineNumber())
                    .append("]")
                    .append("\r\n");
                }
            }
        }
        
        return sb.toString();
    }
    
    public static final void d(String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.d(LOG_TAG, result);
        }
    }

    public static final void e(String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.e(LOG_TAG, "" + result);
        }
    }

    public static final void i(String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.i(LOG_TAG, result);
        }
    }

    public static final void v(String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.v(LOG_TAG, result);
        }
    }

    public static final void w(String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.w(LOG_TAG, result);
        }
    }

    public static final void d(String tag, String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.d(tag, result);
        }
    }
    public static final void e(String tag, String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.e(tag, result);
        }
    }
    
    public static final void e(String tag, Exception ex) {
        if (DEBUG){
            String name = error(ex);
            Log.e(tag, name);
        }
    }
    
    public static final void i(String tag, String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.i(tag, result);
        }
    }
    public static final void v(String tag, String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.v(tag, result);
        }
    }
    public static final void w(String tag, String msg) {
        if (DEBUG){
            String name = getFunctionName();
            String result = (name == null) ? msg: (name + msg);
            Log.w(tag, result);
        }
    }

}

