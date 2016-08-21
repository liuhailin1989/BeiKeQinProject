/**
 * 
 */
package com.android.backchina.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	public static boolean isConnected(Context context) {
    	boolean result = false;
    	
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = connMgr.getActiveNetworkInfo();
		if (ni != null && ni.isConnected()){
			result = true;
		}
		
		return result;
	}

	public static boolean isWifi(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
		if(activeInfo == null)
			return false;
		int networkType = activeInfo.getType();
		if (networkType == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else {
			return false;
		}
	}

}
